(ns clj-pg-plsql.core
  (:require [clojure.contrib.sql  :as sql]
            [rn.clorine.core      :as cl]))

(cl/register-connection! :test-db
     {:driver-class-name "org.postgresql.Driver"
      :user              "jcrean"
      :password          ""
      :url               "jdbc:postgresql://localhost:5432/jc_test"
      })


(cl/with-connection :test-db
  (sql/insert-rows :foobar
                   [1 "thing" "thing2"]
                   [2 "stuff" "more stuff"]))


(defn create-or-replace [name]
  (str "CREATE OR REPLACE FUNCTION " name "() RETURNS integer AS $$"))

(defn begin []
  "BEGIN ")

(defn end []
  "END; ")

(defn close-func []
  "$$ LANGUAGE plpgsql;")

(defmacro defproc [name & body]
  (cl/with-connection :test-db
    (sql/do-commands
     (str (create-or-replace name)
          "DECLARE thing integer;"
          (begin)
          "SELECT id INTO thing FROM foobar WHERE attr1 = 'stuff';"
          "RETURN thing;"
          (end)
          (close-func))))
  (let [call-str (str "select " name "();")]
    (def name (fn []
                  (cl/with-connection :test-db
                    (sql/with-query-results
                      results
                      [call-str]
                      (doall results)))))))

(defproc testing)

(testing)

