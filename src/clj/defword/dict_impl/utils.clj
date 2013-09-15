(ns defword.dict-impl.utils
   (:require [clojure.data.json :as json] ))

(defn encode-str-to-url [s] (java.net.URLEncoder/encode s))


(defn fetch [api-url] (slurp api-url))

(defn json->clj [json] (json/read-str json))

