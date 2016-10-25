Submit
======

```ssh-keygen -f ~/.ssh/javabin-aws```

```eb init submit --region eu-west-1 --keyname javabin-aws --platform java-8```

```eb create prod --region eu-west-1 --instance_type t2.micro --keyname javabin-aws --platform java-8 --scale 2```

