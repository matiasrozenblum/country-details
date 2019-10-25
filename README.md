# nation-details
Nation Details Service
To compile docker image position yourself in the project's root folder and run:

docker build -t nation .

Note that there is a dot at the end, this is part of the command.
To run the docker container run:

docker run --rm --name nation_container -p 9291:9291 nation

Prompt will get "stuck" in the process, if you want to detach it from the process and run it in the background, run it like this:

docker run --rm --name nation_container -p 9291:9291 nation -d

If you want to run it by hand, you need to have Java installed. Once you do, run the file called:

nation-details-jar-with-dependencies.jar

Endpoints example:

http://localhost:9291/country/details?ip=5.6.7.8

http://localhost:9291/statistics
