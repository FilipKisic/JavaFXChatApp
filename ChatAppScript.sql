CREATE DATABASE ChatApp
GO

USE ChatApp
GO

CREATE TABLE Contact
(
	IDContact INT PRIMARY KEY IDENTITY,
	Username NVARCHAR(50) NOT NULL,
	Pswrd NVARCHAR(50) NOT NULL,
	FullName NVARCHAR(50),
	ProfileImage VARBINARY(MAX)
)
GO

CREATE TABLE ContactUser_Contact
(
	ContactUserID INT NOT NULL,
	ContactID INT NOT NULL,
	FOREIGN KEY (ContactUserID) REFERENCES Contact(IDContact),
	FOREIGN KEY (ContactID) REFERENCES Contact(IDContact)
)
GO

CREATE TABLE ChatMessage
(
	IDMessage INT PRIMARY KEY IDENTITY,
	MessageContent VARBINARY(MAX) NOT NULL,
	FromID INT NOT NULL,
	ToID INT NOT NULL,
	TimeOf DATETIME NOT NULL,
	FOREIGN KEY (FromID) REFERENCES Contact(IDContact),
	FOREIGN KEY (ToID) REFERENCES Contact(IDContact)
)
GO

/* CONTACT procedures */
CREATE PROC spCreateContact
	@Username NVARCHAR(50),
	@Pswrd NVARCHAR(50),
	@FullName NVARCHAR(50),
	@ProfileImage VARBINARY(MAX)
AS
BEGIN
	INSERT INTO Contact VALUES (@Username, @Pswrd, @FullName, @ProfileImage)
END
GO

CREATE PROC spUpdateContact
	@ContactUserID INT,
	@FullName NVARCHAR(50)
AS
BEGIN
	UPDATE Contact SET FullName = @FullName WHERE IDContact = @ContactUserID
END
GO

CREATE PROC spSelectContacts
AS
BEGIN
	SELECT * FROM Contact
END
GO

CREATE PROC spAuthenticateContact
	@Username NVARCHAR(50),
	@Pswrd NVARCHAR(50)
AS
BEGIN
	SELECT * FROM Contact WHERE Username = @Username AND Pswrd = @Pswrd
END
GO

CREATE PROC spDeleteContact
	@IDContact INT
AS
BEGIN
	DELETE FROM Contact WHERE IDContact = @IDContact
END
GO

/* CONTACT_USER_CONTACT procedures */
CREATE PROC spAddContact
	@ContactUserID INT,
	@ContactID INT
AS
BEGIN
	INSERT INTO ContactUser_Contact VALUES (@ContactUserID, @ContactID)
END
GO

CREATE PROC spSelectUserContacts
	@ContactUserID INT
AS
BEGIN
	SELECT c.IDContact, c.FullName, c.ProfileImage FROM ContactUser_Contact AS ac
	INNER JOIN Contact AS c ON ac.ContactID = c.IDContact
	WHERE ac.ContactID IN (SELECT ContactID FROM ContactUser_Contact WHERE ContactUserID = @ContactUserID) 
	AND ac.ContactUserID = @ContactUserID
END
GO

/* MESSAGE procedure */
CREATE PROC spCreateMessage
	@MessageContent VARBINARY(MAX),
	@FromID INT,
	@ToID INT,
	@TimeOf DATETIME
AS
BEGIN
	INSERT INTO ChatMessage (MessageContent, FromID, ToID, TimeOf) VALUES (@MessageContent, @FromID, @ToID, @TimeOf)
END
GO

CREATE PROC spSelectConversationMessages
	@UserID INT,
	@ContactID INT
AS
BEGIN
	SELECT * FROM ChatMessage WHERE (FromID = @UserID AND ToID = @ContactID) OR (FromID = @ContactID AND ToID = @UserID)
	ORDER BY TimeOf ASC
END
GO

/* SETUP */
EXEC spCreateContact 'admin', 'algebra', 'Enter Enter', null
EXEC spCreateContact 'john@doe.com', 'johndoe', 'John Doe', null
EXEC spCreateContact 'emma@weasley.com', 'emmaweasley', 'Emma Weasley', null
EXEC spCreateContact 'rachel@yung.com', 'rachelyung', 'Rachel Yung', null
EXEC spCreateContact 'steven@hudson.com', '', 'Steven Hudson', null
EXEC spAddContact 1, 2
EXEC spAddContact 1, 3
EXEC spAddContact 1, 4
EXEC spAddContact 2, 1
EXEC spAddContact 2, 4

/* ADDING IMAGES */
UPDATE Contact SET ProfileImage = (SELECT * FROM OPENROWSET(BULK 'D:\ALGEBRA\5.semestar - Programiranje u Javi 2\ChatApp\src\res\images\profile.png', SINGLE_BLOB) img)
WHERE IDContact = 1
UPDATE Contact SET ProfileImage = (SELECT * FROM OPENROWSET(BULK 'D:\ALGEBRA\5.semestar - Programiranje u Javi 2\ChatApp\src\res\images\contact01.png', SINGLE_BLOB) img)
WHERE IDContact = 2
UPDATE Contact SET ProfileImage = (SELECT * FROM OPENROWSET(BULK 'D:\ALGEBRA\5.semestar - Programiranje u Javi 2\ChatApp\src\res\images\contact02.png', SINGLE_BLOB) img)
WHERE IDContact = 3
UPDATE Contact SET ProfileImage = (SELECT * FROM OPENROWSET(BULK 'D:\ALGEBRA\5.semestar - Programiranje u Javi 2\ChatApp\src\res\images\contact03.png', SINGLE_BLOB) img)
WHERE IDContact = 4

select * from ChatMessage

SELECT * FROM ChatMessage WHERE (FromID = 1 AND ToID = 2) OR (ToID = 1 AND FromID = 2)
ORDER BY TimeOf ASC