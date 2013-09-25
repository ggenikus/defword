(ns defword.dict-impl.dict-org
  (:require [clojure.core.strint :as incub] 
            [defword.dict-impl.utils :as utils] 
            [me.raynes.laser :as laser]))

(defn generate-url [request] 
  (incub/<< "http://www.dict.org/bin/Dict?Form=Dict1&Query="
            "~(utils/encode-str-to-url request)"
            "&Strategy=*&Database=gcide&submit=Submit+query%20Response%20Headersview%20source"))

(defn fetch-from-dict-org [request]
  (utils/fetch (generate-url request)))

(defn remove-links [text]
  (clojure.string/replace text #"(<a href=\".*\">(.*)</a>)" "$2"))
                                                               
  (defn grab-content-from-dict-org-page [text]
  (some-> text
      (laser/parse)
      (laser/select (laser/element= :pre))
      (nth 2 nil)
      (:content)
      laser/fragment-to-html))

(defn dict-org->generic-map [text] 
  (let [content (grab-content-from-dict-org-page text) ]
    (when content
       {:dict :dict-org 
        :link "link" 
        :data {:text (remove-links content)}})))

(defn search-in-dict-org [request]
  (some-> request 
      fetch-from-dict-org 
      dict-org->generic-map))
