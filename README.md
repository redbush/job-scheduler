### Job Scheduler POC

The project contains two services: JobSchedulerApp and JobSchedulerAgent  

**JobSchedulerApp**  
- Contains REST interface to submit jobs
- Jobs are stored in a Redis sorted set
- Jobs are sorted based on a score which is the next execution time
- Jobs that are ready for execution are written to a Redis stream which the JobSchedulerAgent listens on
- After jobs are written to the stream for execution the score is recalculate and the cache is updated

**JobSchedulerAgent**
- Listens for job events on a Redis stream
- When an event is received it executes the provided command

Splitting the applications into standalone services will allow them to be individually scalable.  

##### Submitting a Job
**URL**: http://localhost:8080/submit  
**Method**: POST  
**Content-Type**: application/json  
**Response**: HTTP 200 / no content

##### JSON Fields
- **id**: Job identifier (String)
- **interval**: How often the job executes. TimeUnit defines what the value is. (Integer)
- **timeUnit**: Acceptable values: SECONDS,MINUTES,HOURS,DAYS (String)
- **command**: The shell command to execute (String)

##### Example Body
{"id":"1","interval":"10","timeUnit":"SECONDS","command":"touch /opt/app/test.txt"}  

#### Building and running
Run script: ./run_local.sh

#### Assumptions
- Jobs need to be executed per server/agent
- The jobs are able to run concurrently
- Jobs need to have a backing store

#### Tech Debt
- Add more test coverage
- Refactor repository structure so testable
- Separate Redis for stream 