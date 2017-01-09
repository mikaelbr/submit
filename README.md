# What is this?

This is an application that lets speakers submit talks to JavaZone.

Integrates with [Sleepingpill](https://github.com/javaBin/sleepingPillCore) which stores the talks.

# Getting started

* Check out repo: `git clone git@github.com:javaBin/submit.git`
* Build backend: `cd backend && mvn clean install`
* Build frontend: `cd frontend && npm install`
* Build nodeproxy: `cd scripts/proxy && npm install`
* Run the app: `./scripts/start.sh`

This starts a screen. To escape from the screen: `ctrl-x`, `q`, `y`.

# Running it in IntelliJ

Run the class `SubmitApplication`. Couldn't be easier ;)

# Debugging in Postman

We have created a Postman Collection to make it easy to test all the API calls of Submit when developing.

* Install [Postman](https://www.getpostman.com)
* Import the collection `submit.postman_collection.json`
* Setup Postman environment with key-value: `baseurl=https://submit.javazone.no` or `baseurl=http://localhost:8080`
* Start by running the `POST – Generate token` with your email address.
* Then you can do any other call using the token you got in an email in an `X-token` header.


# Deployment to AWS

## First time: configure AWS

Install `aws` and `eb` command line tools:

```
brew install awscli
brew install aws-elasticbeanstalk
```


Edit/add the file `~/.aws/credentials` and add these lines

```
[javabin]
aws_access_key_id = <ADD YOURS HERE>
aws_secret_access_key = <ADD YOURS HERE>
```

Initialize your Elastic Beanstalk environment: 
```
cd backend
eb init --region eu-central-1 --profile javabin
```

Just select the existing submit environment

## Every time: deployment

- `cd frontend && ./deploy.sh`
- `cd backend && ./deploy.sh`

Backend deploy needs vault password. Ask around to get it :)

## Creating a new environment

You could probably just use test/prod which exists. 

If you need a new, do this and follow the instructions to get a backend environment up and running
```
eb create --region eu-central-1 --profile javabin
```

You need to setup a database + property files for backend + a frontend S3 bucket + load balancer as well. For now, this is a "manual process". Ask around... ;)
