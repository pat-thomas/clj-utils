(ns clj-utils.lang-utils)

(defmacro defmapper
  [name impl]
  `(defn ~name
     [anon#]
     (map ~impl anon#)))
