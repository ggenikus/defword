(ns defword.dict-test
  (:require [defword.dict-impl.dict-org :as dict] 
            [defword.dict-impl.urban :as urb]
            [defword.dict-impl.yandex :as ya]
            [defword.dict :refer :all]
            [midje.sweet :refer :all]
            [defword.dict-impl.utils :as utils]
            [clojure.data.json :as json]))

(fact "Fetch from yandex should generate url and fethc data"
      (ya/fetch-from-yandex ..request.. ..lang..) => truthy 
      (provided 
        (ya/generate-url ..request.. ..lang..) => "url"
        (utils/fetch "url") => "data"))

(fact "Fetch from dict-org should generate url and fethc data"
      (dict/fetch-from-dict-org ..request.. ) => truthy 
      (provided 
        (dict/generate-url ..request.. ) => "url"
        (utils/fetch "url") => "data"))


(fact "Fetch from urban should generate url and fethc data"
      (urb/fetch-from-urban ..request.. ) => truthy 
      (provided 
        (urb/generate-url ..request..) => "url"
        (utils/fetch "url") => "data"))


(def urban-response {"tags"["school""exam"], 
                     "result_type" "exact", 
                     "list"[{"defid" 708924, 
                             "thumbs_down" 164, 
                             "example" "This is a test message",
                             "word" "test",
                             "definition" "A process for testing things" }
                            {"defid" 2957653,
                             "example""some example",
                             "word" "test",
                             "current_vote" "",
                             "definition""some definition",
                             "thumbs_up" 133}]})

(fact "Should transform datastructure from urban dictionary to generic map"
                    (urb/urban->generic-map urban-response) => {:dict :urban
                                                            :link "link"
                                                            :data [{:definition "A process for testing things", 
                                                                    :example "This is a test message"} 
                                                                   {:definition "some definition", 
                                                                    :example "some example"}]})

(fact "Search in dectionnary should call one of dict-search implementation"
      (fact "Let's try urban "
            (search-in :urban ..request..) => truthy
            (provided
              (urb/search-in-urban ..request..) => "response"))

      (fact "Let's try yandex " 
            (search-in :yandex ..request.. ..lang..) => truthy
            (provided
              (ya/search-in-yandex ..request.. ..lang..) => "response"))

      (fact "Let's try dict-org" 
            (search-in :dict-org ..request..) => truthy 
            (provided
              (dict/search-in-dict-org ..request..) => "response"))

      (fact "Let's try not exisitng dictionary "
            (search-in :no-exisiting ..request..) => nil))

