FROM openjdk:8
MAINTAINER Brian Rosedale <brian.j.rosedale@gmail.com>

ENV APP_HOME /opt/app
RUN mkdir -p $APP_HOME
COPY job-scheduler-app/target/job-scheduler-app*.jar $APP_HOME/
RUN mv $APP_HOME/job-scheduler-app*.jar $APP_HOME/job-scheduler-app.jar
RUN chmod 777 $APP_HOME/job-scheduler-app.jar

WORKDIR $APP_HOME
EXPOSE 8080

CMD ["java", "-Dspring.profiles.active=docker", "-jar", "job-scheduler-app.jar"]
