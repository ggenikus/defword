(ns defword.core 
  (:require [compojure.handler :as handler]
            [compojure.route :as route]
            [compojure.core :refer :all]
            [ring.util.response :as resp]
            [shoreleave.middleware.rpc :refer [defremote wrap-rpc]]
            [defword.dict :as dict]))

(defremote translate [word] (if (dict/eng? word) 

                             (map deref 
                                  [(future (dict/search-in :yandex word :en-ru)) 
                                   (future  (dict/search-in :urban word)) 
                                   (future  (dict/search-in :dict-org word))])

                              (let [ya-ru-en (dict/search-in :yandex word :ru-en)
                                     ya-ru-en-word (apply str (:data ya-ru-en)) ]
                                   (conj (map deref [(future (dict/search-in :urban ya-ru-en-word)) 
                                                     (future (dict/search-in :dict-org ya-ru-en-word)) ]) ya-ru-en))))


(defroutes defword-routes
  (GET "/" [] (resp/resource-response  "public/defword-app.html")) 
  (GET "/translate/:word" [word] word)
  (route/resources "/"))

(def app
  (-> (handler/site defword-routes)
      (wrap-rpc)
      (handler/site)))