(ns defword.dict-impl.urban
  (:require [clojure.core.strint :as incub] 
            [clojure.walk :as walk] 
            [defword.dict-impl.utils :as utils]))

(defn generate-url [request] 
  (incub/<< "http://api.urbandictionary.com/v0/define?term="
            "~(utils/encode-str-to-url request)"))

(defn fetch-from-urban [request]
    (utils/fetch (generate-url request)))

(defn urban->generic-map [{:strs [list]}] 
  {:dict :urban
   :link "link"
   :data (map (comp walk/keywordize-keys #(select-keys % ["example" "definition"])) 
              list)})

(defn search-in-urban [request]
  (-> request 
      fetch-from-urban
      utils/json->clj
      urban->generic-map))
