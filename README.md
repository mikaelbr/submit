# What is this?

This is an application that lets speakers submit talks to JavaZone.

Integrates with [Sleepingpill](https://github.com/javaBin/sleepingPillCore) which stores the talks.

# Getting started

* Check out repo: `git clone git@github.com:javaBin/submit.git`
* Build backend: `cd backend && mvn clean install`
* Build frontend: `cd frontend && npm install`
* Create config file and fill out password: `cp backend/configuration-template.yaml backend/configuration.yaml && vi backend/configuration.yaml`
* Run the app: `./scripts/start.sh`

This starts a screen. To escape from the screen: `ctrl-x`, `q`, `y`.

# Running it in IntelliJ

Run the class `SubmitApplication`. Couldn't be easier ;)

# Debugging in Postman

We have created a Postman Collection to make it easy to test all the API calls of Submit when developing.

* Install [Postman](https://www.getpostman.com)
* Import the collection `submit.postman_collection.json`
* Start by running the `POST – DEBUG login`, then you can do any other call.

# Configure AWS

Install `aws` and `eb` command line tools:

```
brew install aws
brew install eb
```

Configure a AWS profile for javabin:


`~.aws/credentials`

```
[javabin]
aws_access_key_id = <ADD YOURS HERE>
aws_secret_access_key = <ADD YOURS HERE>
```

`~.aws/config`

```
[default]
region = eu-central-1
```

Run `eb init` in /backend

## Deployment

See the "Configure AWS" section first

- `cd frontend && ./deploy.sh`
- `cd backend && ./deploy.sh`
