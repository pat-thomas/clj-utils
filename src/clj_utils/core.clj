(ns clj-utils.core
  (:require [clj-utils.macros :refer :all]))

(defn require-all-in-ns
  [ns-name]
  `(require (clj-utils [~ns-name :refer :all])))

(defmacro alias-namespaces
  [& namespaces]
  `(do ~@(map (fn [ns-name]
                (require-all-in-ns ns-name))
              namespaces)))

(alias-namespaces)
