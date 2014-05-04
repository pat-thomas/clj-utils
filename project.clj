(defproject clj-utils "0.1.0"
  :description  "A collection of various utility functions that I've found helpful in doing Clojure development."
  :url          "http://github.com/pat-thomas/clj-utils"
  :license      {:name "Eclipse Public License"
                 :url "http://www.eclipse.org/legal/epl-v10.html"}
  :repositories [["releases" {:url   "http://github.com/pat-thomas/clj-utils"
                              :creds :gpg}]]
  :dependencies [[org.clojure/clojure       "1.6.0"]
                 [org.clojure/tools.logging "0.2.6"]])
