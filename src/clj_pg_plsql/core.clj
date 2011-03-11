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
  true)

