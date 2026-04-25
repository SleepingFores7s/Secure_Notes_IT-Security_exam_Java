DROP DATABASE IF EXISTS Secure_Notes;

CREATE DATABASE Secure_Notes;

USE Secure_Notes;

CREATE TABLE User
(
    ID       INT                             NOT NULL PRIMARY KEY AUTO_INCREMENT,
    Username VARCHAR(25) COLLATE utf8mb4_bin NOT NULL UNIQUE, -- COLLATE utf8mb_bin makes username Case sensitive.
    Password VARCHAR(50)                     NOT NULL,
    Role     VARCHAR(10) CHECK (role IN ('USER', 'ADMIN'))
);

CREATE TABLE Note
(
    ID           INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    User_ID      INT,
    Created_Date TIMESTAMP,
    Last_Edited  TIMESTAMP,
    Title        VARCHAR(100),
    Stored_Data  LONGTEXT,
    FOREIGN KEY (User_ID) REFERENCES User (ID) ON DELETE SET NULL
);

INSERT INTO User(Username, Password, Role)
VALUES ('Hanna', 'fc4b302b', 'USER'), -- pass = 123
       ('Jenny', 'fc4bab4b', 'USER'), -- pass = 234
       ('Gwen', 'fc4c476b', 'USER'),-- pass = 345
       ('Daniel', 'fc4b302b', 'ADMIN'); -- pass = 123

INSERT INTO Note(User_ID, Created_Date, Last_Edited, Title, Stored_Data)
VALUES
    -- Hanna (User_ID = 1)
    (1, '2024-01-10 12:30:00', '2024-01-10 12:30:00', 'Grocery list',
     'Milk (2.5% fat), whole‑grain bread, 12-pack eggs, bananas, pasta, tomato sauce, coffee filters.'),
    (1, '2024-01-12 09:15:00', '2024-01-12 09:20:00', 'Dentist reminder',
     'Call the dentist to reschedule the cleaning appointment; ask for times after 16:00 and check insurance coverage for fluoride treatment.'),
    -- Jenny (User_ID = 2)
    (2, '2024-02-01 14:00:00', '2024-02-01 14:00:00', 'Workout plan',
     'Weekly plan: Monday upper body strength, Tuesday 5 km run, Thursday yoga, Saturday full-body HIIT; track progress in the fitness app.'),
    (2, '2024-02-03 18:45:00', '2024-02-03 18:50:00', 'Weekend trip ideas',
     'Destinations to consider: Gothenburg archipelago, hiking in Dalsland, spa day in Sunne; check weather and book accommodation early.'),
    -- Gwen (User_ID = 3)
    (3, '2024-03-05 11:10:00', '2024-03-05 11:10:00', 'SQL study notes',
     'Review normalization (1NF–3NF), primary vs foreign keys, JOIN types, ER diagram cardinality, stored procedure basics, and sample schemas.'),
    (3, '2024-03-06 16:25:00', '2024-03-06 16:30:00', 'Project presentation draft',
     'Outline: introduction, problem statement, solution architecture, database schema, UI mockups, timeline; add diagrams and speaker notes.'),
    -- Daniel (User_ID = 4)
    (4, '2024-04-01 20:00:00', '2024-04-01 20:00:00', 'Wallpaper Engine To-Do',
     'Remove unused workshop items, reorganize playlists, test performance impact of video wallpapers, export backup of favorite configurations.'),
    (4, '2024-04-02 22:10:00', '2024-04-02 22:15:00', 'Coding project ideas',
     'Concepts: modular notes app with image support, controller-mapping visualizer, Volvo V70 diagnostic helper, Bootstrap grid generator.');


-- Stored Procedures-------------------------------

-- Create new user
DELIMITER //
CREATE PROCEDURE CreateNewUser(
    IN P_User_Username VARCHAR(25),
    IN P_User_Password VARCHAR(50),
    OUT P_User_ID INT,
    OUT P_User_Role VARCHAR(10),
    OUT P_Status_Message VARCHAR(50))
BEGIN

    DECLARE EXIT HANDLER FOR SQLEXCEPTION
        BEGIN
            ROLLBACK;
            SET AUTOCOMMIT = 1;
            SET P_Status_Message = '--DB: Error in: CreateNewUser()--';
        END;

    SET AUTOCOMMIT = 0;
    START TRANSACTION;

    -- Checks if Username already exist
    IF EXISTS(SELECT ID FROM user WHERE Username = P_User_Username) THEN
        SET P_User_Role = NULL;
        SET P_User_ID = NULL;
        SET P_Status_Message = '--DB: Username taken--';
    ELSE
        INSERT INTO User(USERNAME, PASSWORD, ROLE)
        VALUES (P_User_Username, P_User_Password, 'USER');
        SET P_Status_Message = '--DB: User successfully created--';
        SELECT ID, ROLE
        INTO P_User_ID, P_User_Role
        FROM User
        WHERE Username = P_User_Username
          AND Password = P_User_Password;

        COMMIT;
    END IF;

END //
DELIMITER ;

-- Login w. Credentials --
DELIMITER //
CREATE PROCEDURE LoginUser(
    IN P_Username VARCHAR(25),
    IN P_Password VARCHAR(50),
    OUT P_Role VARCHAR(10),
    OUT P_User_ID INT,
    OUT P_Status_Message VARCHAR(50))

BEGIN
    -- Fetch values
    SELECT ROLE, ID
    INTO P_Role, P_User_ID
    FROM User
    WHERE Username = P_Username
      AND Password = P_Password;

    -- Checks if used_ID is Null
    IF P_User_ID IS NULL THEN
        SET P_Status_Message = '-- User Account not found --';
    ELSE
        SET P_Status_Message = '-- User Account Found --';

    END IF;
END //
DELIMITER ;

-- Get One Users Notes
DELIMITER //

CREATE PROCEDURE GetNotesForUser(IN P_User_ID INT)
BEGIN
    SELECT n.ID,
           n.Title,
           n.Stored_Data,
           u.Username AS owner_name
    FROM note n
             JOIN user u ON n.user_id = u.id
    WHERE n.user_id = P_User_ID;
END //

DELIMITER ;

-- Get All notes
DELIMITER //

CREATE PROCEDURE GetNotesForAdmin()
BEGIN
    SELECT n.ID,
           n.Title,
           n.Stored_Data,
           u.Username AS owner_name
    FROM note n
             JOIN user u ON n.user_id = u.id;
END //

DELIMITER ;

-- Delete One Note
DELIMITER //

CREATE PROCEDURE DeleteNote(
    IN P_User_ID INT,
    IN P_User_Role VARCHAR(10),
    IN P_Note_ID INT,
    OUT P_Was_Deleted BOOLEAN)
BEGIN
    DECLARE rowAffected INT DEFAULT 0;

    IF P_User_Role = 'ADMIN' THEN
        DELETE
        FROM note
        WHERE note.ID = P_Note_ID;
        SET rowAffected = ROW_COUNT();
        SET P_Was_Deleted = (rowAffected > 0); -- True = 1/False = 0
    ELSE
        DELETE
        FROM note
        WHERE note.ID = P_Note_ID
          AND note.User_ID = P_User_ID;
        SET rowAffected = ROW_COUNT();
        SET P_Was_Deleted = (rowAffected > 0); -- True = 1/False = 0

    END IF;

END//

DELIMITER ;

-- Change User Password
DELIMITER //

CREATE PROCEDURE ChangePassword(
    IN P_Username VARCHAR(25),
    IN P_New_Password VARCHAR(50),
    OUT P_Password_Changed BOOLEAN)
BEGIN
    DECLARE rowAffected INT DEFAULT 0;

    UPDATE user
    SET user.Password = P_New_Password
    WHERE Username = P_Username;
    SET rowAffected = ROW_COUNT();
    SET P_Password_Changed = (rowAffected > 0); -- True = 1/False = 0

END//

DELIMITER ;

-- Create a new Note
DELIMITER //

CREATE PROCEDURE CreateNewNote(
    IN P_User_ID INT,
    IN P_Title VARCHAR(100),
    IN P_Body LONGTEXT,
    OUT P_Note_Created BOOLEAN)
NoteCreate: BEGIN -- Create a named block
    DECLARE rowAffected INT DEFAULT 0;

    IF P_Title IS NULL OR P_Title = '' THEN
        SET P_Note_Created = FALSE;
        LEAVE NoteCreate; -- Leaves named block
    END IF;

    INSERT INTO note (User_ID,
                      Created_Date,
                      Last_Edited,
                      Title,
                      Stored_Data)
    VALUES (P_User_ID,
            NOW(),
            NOW(),
            P_Title,
            P_Body);
    SET rowAffected = ROW_COUNT();
    SET P_Note_Created = (rowAffected > 0); -- True = 1/False = 0

END NoteCreate//

DELIMITER ;

-- Change User Note
DELIMITER //

CREATE PROCEDURE EditNote(
    IN P_User_ID INT,
    IN P_Note_ID INT,
    IN P_Title_Replacer VARCHAR(100),
    IN P_Body_Replacer LONGTEXT,
    OUT P_Note_Updated BOOLEAN)
BEGIN
    DECLARE rowAffected INT DEFAULT 0;

    UPDATE Note
    SET note.Title       = P_Title_Replacer,
        note.Stored_Data = P_Body_Replacer,
        note.Last_Edited = NOW()
    WHERE User_ID = P_User_ID
      AND ID = P_Note_ID;
    SET rowAffected = ROW_COUNT();
    SET P_Note_Updated = (rowAffected > 0); -- True = 1/False = 0

END//

DELIMITER ;