# !/bin/bash

NUM_OF_ARGS=$#
is_migrate=0

load_schema() {
    echo "Loading the anime schema into grakn"
    # Load schema into grakn
    ~/grakn-dist-1.2.0/graql console -f anime-schema.gql -k anime
}

migrate_anime() {
    echo "Migrating the anime.csv to grakn"
    sleep 5s
    # Migrate the anime.csv to grakn
    ~/grakn-dist-1.2.0/graql migrate csv -i csv/anime.csv -t migrator/anime-migrator.gql -k anime
}

migrate_genre() {
    echo "Migrating the genre.csv to grakn"
    sleep 5s
    # Migrate the genre.csv to grakn
    ~/grakn-dist-1.2.0/graql migrate csv -i csv/genre.csv -t migrator/genre-migrator.gql -k anime
}

migrate_anime_genre() {
    echo "Migrating the anime_genre.csv to grakn"
    sleep 5s
    # # Migrate the anime_genre.csv to grakn
    ~/grakn-dist-1.2.0/graql migrate csv -i csv/anime_genre.csv -t migrator/genre-anime-migrator.gql -k anime
}

migrate_user() {
    echo "Migrating the user.csv to grakn"
    sleep 5s
    # Migrate the user.csv to grakn
    ~/grakn-dist-1.2.0/graql migrate csv -i csv/user.csv -t migrator/user-migrator.gql -k anime
}

migrate_user_anime() {
    # Migrate the user_anime.csv files to grakn
    for csv_file in csv/user_anime_*.csv
    do
        echo "Migrating the ${csv_file} to grakn"
        ~/grakn-dist-1.2.0/graql migrate csv -i $csv_file -t migrator/user-anime-migrator.gql -k anime
        sleep 5s
    done
}

while [ ! $NUM_OF_ARGS -eq 0 ]
do
	case "$1" in
		--schema | -f)
			load_schema
			;;
		--migrate | -m)
            is_migrate=1
			;;
        anime)
            if [ $is_migrate -eq 0 ]; then
                echo "Missing --migrate or -m flag"
                exit
            fi

            migrate_anime
            ;;
        genre)
            if [ $is_migrate -eq 0 ]; then
                echo "Missing --migrate or -m flag"
                exit
            fi

            migrate_genre
            ;;
        anime_genre)
            if [ $is_migrate -eq 0 ]; then
                echo "Missing --migrate or -m flag"
                exit
            fi

            migrate_anime_genre
            ;;
        user)
            if [ $is_migrate -eq 0 ]; then
                echo "Missing --migrate or -m flag"
                exit
            fi
            
            migrate_user
            ;;
        anime_user)
            if [ $is_migrate -eq 0 ]; then
                echo "Missing --migrate or -m flag"
                exit
            fi

            migrate_user_anime
            ;;
        *)  
            if [ -z $1 ]; then
                exit
            fi
            echo "Unexpected option $1"
            exit 1
            ;;
	esac
	shift
done