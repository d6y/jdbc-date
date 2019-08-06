Example of inserting and reading back a LocalDate using Slick,
where a date we can write cannot be read back.

It's a bit of a corner case in that it's the year 0
which doesn't exist, but is valid according to LocalDate.

To runs this code you need:

1. A postgress database instance

2. Change `src/main/resources/application.conf` to set the database name, username, and password.

3. Have this table:

        create table "foo" ( "dob" DATE );

And then:

````
$ sbt run
...
[info] Done compiling.
[info] Running Example
LocalDate formatted value is: 0000-12-31
SQL insert is:  insert into "foo" values ('0001-12-31 BC +00')
The value read back is: 0001-12-31 BC
Parsing value produces: Failure(java.time.format.DateTimeParseException: Text '0001-12-31 BC' could not be parsed, unparsed text found at index 10)
```




