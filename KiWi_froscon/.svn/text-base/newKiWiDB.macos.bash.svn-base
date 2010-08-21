#!/bin/bash
#set -xv

# the postgres super user - you may change it according with your needs. 
POSTGRES_SUPER_USER=postgres

dropdb -h localhost -p 5432 kiwi -U $POSTGRES_SUPER_USER
echo "The KiWi db was droped"
createdb -h localhost -p 5432 -U $POSTGRES_SUPER_USER -O kiwi kiwi
echo "The KiWi db new created"
