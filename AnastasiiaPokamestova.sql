-- create tables

CREATE TABLE Books (
	BOOKID integer Not Null,
	AUTOR varchar2 (30) NOT NULL ,
	TITLE varchar2 (30) NOT NULL
	);
		
	Alter Table Books
		Add (Primary Key(BOOKID));
        
CREATE TABLE homeLibrary (
	FIOid integer Not Null,
	FIO varchar2 (30) NOT NULL ,
	qount integer NOT NULL,
    bookid integer Not Null
	);
    
Alter Table homeLibrary
		Add (FOREIGN KEY (BOOKID) REFERENCES Books (BOOKID)); 
        
-- add values
INSERT INTO books (BOOKID,autor,title) VALUES(1, 'N.V.Gogol','Dead souls');
INSERT INTO books (BOOKID,autor,title) VALUES(2, '?.?.??????','??????? ??????');  
INSERT INTO books (BOOKID,autor,title) VALUES(3, 'L.N.Tolstoy','War and peace');
INSERT INTO books (BOOKID,autor,title) VALUES(4, 'F.M.Dostoevsky','Crime and Punishment');

INSERT INTO homeLibrary (FIOid,FIO,qount,bookid) VALUES(10000, 'Pokamestova A.M.',1,1);
INSERT INTO homeLibrary (FIOid,FIO,qount,bookid) VALUES(10001, 'Pokamestova A.M.',1,2);
INSERT INTO homeLibrary (FIOid,FIO,qount,bookid) VALUES(10001, 'Ivanov I.I.',1,3);
INSERT INTO homeLibrary (FIOid,FIO,qount,bookid) VALUES(10001, 'Sharov A.I.',1,4);
        
        
        
        
        
        
        
        
        
        
        
        
        