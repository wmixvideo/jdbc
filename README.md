JDBC Helper
====

##About
The JDBC library is a non-intrusive way to facilitate the development of Java software.

Allied with the DAO pattern, it helps achieve maximum productivity and performance by allowing developers to write unique queries and iterate over the SQLs results with columns with same name present in different tables.

##Installation
The JDBC library can be used simply putting the project jar into your classpath, or using maven:

```xml
<repositories>
    <repository>
        <id>jdbc-mvn-repo</id>
        <url>https://raw.github.com/fincatto/jdbc/mvn-repo/</url>
        <snapshots>
            <enabled>true</enabled>
            <updatePolicy>always</updatePolicy>
        </snapshots>
    </repository>
</repositories>
```

##Compatibility
Designed with compatibility in mind, extending the JDBC standard protocols, can be used with any relational database. 

##Classes
###DFResultSet
Extends the standard ResultSet interface, adding features like searching by table + column.

###How to use
####Creating the sql structure:
 
 ```sql
 CREATE TABLE person (
   id serial NOT NULL,
   name character varying,
   CONSTRAINT person_pkey PRIMARY KEY (id)
 );
 ```
 
 ```sql
 CREATE TABLE client (
   id serial NOT NULL,
   id_person integer NOT NULL,
   trading_name character varying,
   CONSTRAINT client_pkey PRIMARY KEY (id),
   CONSTRAINT client_id_person_fkey FOREIGN KEY (id_person)
       REFERENCES person (id) MATCH SIMPLE
       ON UPDATE CASCADE ON DELETE CASCADE
 );
 ```
 
 ```sql
 insert into person (name) values ('Person 1');
 insert into person (name) values ('Person 2');
 ```
 
 ```sql
 insert into client (id_person, trading_name) values (1, 'Company 1');
 insert into client (id_person, trading_name) values (2, 'Company 2');
 insert into client (id_person, trading_name) values (1, 'Company 3');
 ```

 ```sql
 select *
 from person p
 join client c on (c.id_person = p.id);
 ```

####Example

 ```java
 public static void main(String[] args) {
     try (Statement stmt = getConnection().createStatement()) {
         try (DFResultSet rs = new DFResultSet(stmt.executeQuery("select * from person p join client c on (c.id_person = p.id)"))) {
             final List<Client> clients = new ArrayList<>();
             while (rs.next()) {
                 final Client client = parseClient(rs, "client");
                 client.setPerson(parsePerson(rs, "person"));
                 clients.add(client);
             }
             for(Client client : clients) {
                 System.err.println(client);
             }
         }
     } catch (Exception e) {
         e.printStackTrace();
     }
 }
 ```
 
 ```java
 private static Person parsePerson(final DFResultSet rs, final String tableName) throws SQLException {
     final Person person = new Person();
     person.setId(rs.getInt(tableName, "id"));
     person.setName(rs.getString(tableName, "name"));
     return person;
 }
 ```
 
 ```java
 private static Client parseClient(final DFResultSet rs, final String tableName) throws SQLException {
     final Client client = new Client();
     client.setId(rs.getInt(tableName, "id"));
     client.setTradingName(rs.getString(tableName, "trading_name"));
     client.setPerson(new Person(rs.getInt(tableName, "id_person")));
     return client;
 }
 ```
 
 ```java
 public class Client {
 
     private int id;
     private Person person;
     private String tradingName;
 
     public int getId() {
         return id;
     }
 
     public void setId(int id) {
         this.id = id;
     }
 
     public Person getPerson() {
         return person;
     }
 
     public void setPerson(Person person) {
         this.person = person;
     }
 
     public String getTradingName() {
         return tradingName;
     }
 
     public void setTradingName(String tradingName) {
         this.tradingName = tradingName;
     }
 
     @Override
     public String toString() {
         return "Client{" +
                 "id=" + id +
                 ", tradingName='" + tradingName + '\'' +
                 ", person=" + person +
                 '}';
     }
 }
 ```
 
 ```java
 public class Person {
 
     private int id;
     private String name;
 
     public Person() {
     }
 
     public Person(int id) {
         this.id = id;
     }
 
     public int getId() {
         return id;
     }
 
     public void setId(int id) {
         this.id = id;
     }
 
     public String getName() {
         return name;
     }
 
     public void setName(String name) {
         this.name = name;
     }
 
     @Override
     public String toString() {
         return "Person{" +
                 "id=" + id +
                 ", name='" + name + '\'' +
                 '}';
     }
 }
 ```

