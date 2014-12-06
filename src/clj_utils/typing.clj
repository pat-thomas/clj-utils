(ns clj-utils.typing
  (:refer-clojure :exclude [defn])
  (:require [clojure.core.typed :as t :refer [ann cf check-ns defalias defn]]))



;; always type check on compile
(check-ns)





