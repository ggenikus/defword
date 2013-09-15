(ns defword.dict
 (:require [defword.dict-impl.yandex :as ya-impl ]
           [defword.dict-impl.urban :as urban-impl ]
           [defword.dict-impl.dict-org :as dict-org-impl]))


(defn search-in [dict request & params] 
  (when-let [dict-impl (dict {:yandex ya-impl/search-in-yandex
                              :urban urban-impl/search-in-urban
                              :dict-org dict-org-impl/search-in-dict-org})] 
    (apply dict-impl request params)))

(defn eng? [s]
  (-> "US-ASCII"
      java.nio.charset.Charset/forName 
      .newEncoder
      (.canEncode s)))