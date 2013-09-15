(ns defword.utils-test
  (:require [defword.dict-impl.utils :refer :all]
            [midje.sweet :refer :all]))

(fact "encode-str-to-url should translates string into application/x-www-form-urlencoded format "
  (encode-str-to-url "test") => "test"
  (encode-str-to-url "test me") => "test+me")

(fact "eng? should detect is string contains only latin letters"
      (eng? "qwertyuiopasdfghjklzxcvbnm") => true
      (eng? "привет") => false
      (eng? "hell привет") => false)

