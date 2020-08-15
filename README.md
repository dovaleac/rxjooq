# Purpose

Connecting to jooq in a reactive manner, and retrieving data in
RxJava structures, like `Flowable`, `Single`, `Maybe` and 
`Completable`.

# Usage

The entrypoint is the `RxJooqFacade` class.

## Obtaining an instance of `RxJooqRunner`

After you obtain your `RxJooqFacade` instance, you have to specify
a way of connecting to the database (there are several ways of 
achieving this, all of them in the `RxJooqFacade` class) and, 
optionally, an executor, to run threads:

```java
RxJooqRunner runner = rxJooqFacade
    .withConnectionAndDialect(connection, SQLDialect.POSTGRES)
    .withExecutor(executor); //this line is optional
```

## Retrieving a single record

Typically, when you query a database, you cannot be sure whether
it will return a record or not, due to the query filters. For this
reason, the method returns a `Maybe`:

```java
Maybe<GameRecord> gameRecord = rxJooqRunner.queryFirstResultIfAny(
    dslContext -> dslContext
        .selectFrom(GAME)
        .where(GAME.ROOM_ID.eq(roomId))
        .and(GAME.STATUS.eq(GameStatus.CREATED.name()));
```

If you are sure that there's at least one record, you can do this instead:

```java
Single<GameRecord> gameRecord = rxJooqRunner.queryFirstResult(
    dslContext -> dslContext
        .selectFrom(GAME)
        .where(GAME.ROOM_ID.eq(roomId))
        .and(GAME.STATUS.eq(GameStatus.CREATED.name()));
```

## Retrieving several records

In this case, we'll return all the matched elements:

```java
Flowable<GameRecord> gameRecords = rxJooqRunner.querySeveralResults(
    dslContext -> dslContext
        .selectFrom(GAME)
        .where(GAME.ROOM_ID.eq(roomId))
        .and(GAME.STATUS.eq(GameStatus.CREATED.name()));
```

## Running a query which doesn't return anything

Given that we don't care about the result, we'll return a
`Completable`:

```java
Completable insertAnswer = rxJooqRunner.executeReactive(
    dslContext ->
       dslContext
           .insertInto(ANSWER)
           .columns(ANSWER.ANSWER_)
           .values(answer));
```

# Constraints

You have to tell your dependency injector to inject 
`RxJooqFacadeImpl` as `RxJooqFacade` and
`DslContextAdapterImpl` as `DslContextAdapter`.


