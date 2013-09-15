(ns defword.app
  (:require [clojure.browser.repl :as repl ]
            [domina :as dom]
            [domina.events :as ev]
            [domina.xpath :as xp]
            [cljs.reader :refer [read-string]]
            [shoreleave.remotes.http-rpc :refer [remote-callback]]))

(defn generate-panel [name html-class f]
  (str "<div class='translate panel "html-class " '>"
          "<div class='panel-heading'><h5>"name"</h3></div>"
          "<div class='panel-body'>"(f)"</div>"
       "</div>"))

(defn generate-content-for-urban [data]
  (let [content (get-in data [:data])] 
    (apply str (map #(let [example-str (:example %)
                           defin-str (:definition %)] 
                       (str "<pre>"
                         (if defin-str (str "<span class='badge'>Definition:</span>"
                                               "<p>"defin-str"</p>"))
                            (if example-str (str "<span class='badge'>Example:</span>"
                                                 "<p>"example-str"</p>")) 
                            "</pre>"))
                    content))))

(defn generate-content-for-dict-org [data] 
  (let [content (get-in data [:data :text])]
    (if content 
      (str "<pre>" content "</pre>"))))

(defn generate-content-for-yandex [data] 
  (let [content (apply str (:data data))]
    (str "<pre>"content"</pre>")))


(defmulti get-search-result :dict)

(defmethod get-search-result :urban [data] (generate-panel "Urbandictionary"
                                                           "panel-primary"
                                                           #(generate-content-for-urban data)))
(defmethod get-search-result :yandex [data] (generate-panel "Yandex translate" 
                                                            "panel-success"
                                                            #(generate-content-for-yandex data)))
(defmethod get-search-result :dict-org [data] (generate-panel "Dict.org"
                                                              "panel-warning"
                                                              #(generate-content-for-dict-org data)))

(defn render-elements [htmlstr]
  (dom/destroy! (dom/by-class "translate"))
  (dom/append! (dom/by-id "search-results") htmlstr ))


(defn render-data [data] (render-elements (apply str (map get-search-result data))))


(defn show-loading-inidcator [] (dom/set-styles! (dom/by-id "loading") {:display ""}))
(defn hide-loading-inidcaotr [] (dom/set-styles! (dom/by-id "loading") {:display "none"}))

(defn print-value [] 
  (let [text (str (dom/value (dom/by-id "search-inp")))]
    (show-loading-inidcator)
    (dom/set-styles! (dom/by-id "loading") {:display "visible"})
    (remote-callback :translate [text] 
                     (fn [arg] 
                       (hide-loading-inidcaotr)
                       (render-data arg) 
                       (.scrollIntoView (.getElementById js/document "search-inp"))))))


(defn ^:export init []
  (if (and js/document (.-getElementById js/document)) 
    (do 
      (ev/listen! (dom/by-id "translate") :click print-value))))


