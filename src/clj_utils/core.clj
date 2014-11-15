(ns clj-utils.core)

(defn- macro?
  [sym]
  (->> sym (ns-resolve *ns*) meta :macro))

(defn- ->aliased-fn-sym
  [ns-sym fn-name]
  (-> ns-sym (str "/" fn-name) symbol))

(defn- produce-alias-defs-list
  [ns-sym]
  (let [fns-in-ns (-> ns-sym ns-publics keys)]
    (->> fns-in-ns
         (filter (fn remove-macros [fn-name]
                   (not (macro? (->aliased-fn-sym ns-sym fn-name)))))
         (map (fn build-alias [fn-name]
                (when-not (macro? (symbol fn-name))
                  (let [aliased-fn-sym (->aliased-fn-sym ns-sym fn-name)]
                    `(def ~fn-name ~aliased-fn-sym))))))))

(defmacro alias-all-in-ns
  [namespace]
  (let [ns-sym (-> namespace name symbol)]
    (do (require ns-sym)
        `(do ~@(produce-alias-defs-list ns-sym)))))

(alias-all-in-ns clj-utils.lang-utils)
(alias-all-in-ns clj-utils.macros)
(alias-all-in-ns clj-utils.maps)
(alias-all-in-ns clj-utils.random)
(alias-all-in-ns clj-utils.seq-utils)
