(ns defword.dict-impl.yandex
    (:require [clojure.core.strint :as incub] 
              [defword.dict-impl.utils :as utils] 
              [defword.properties :as prop]))

(defn generate-url [request lang] 
  (let [ya-api-key  (:yandex-api-key prop/app-properties)] 
    (-> (incub/<< "https://translate.yandex.net/api/v1.5/tr.json/translate?key=~{ya-api-key}&lang=~{lang}&text="
                "~(utils/encode-str-to-url request)"))))

(defn fetch-from-yandex [request lang] (utils/fetch (generate-url request lang)))

(defn yandex->generic-map [{:strs [text]}]
  {:dict :yandex
   :link "link"
   :data text})

(defn search-in-yandex [request lang]
  (-> request 
      (fetch-from-yandex (name lang))
      utils/json->clj
      yandex->generic-map))

