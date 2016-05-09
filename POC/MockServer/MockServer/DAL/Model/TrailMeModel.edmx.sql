
-- --------------------------------------------------
-- Entity Designer DDL Script for SQL Server 2005, 2008, 2012 and Azure
-- --------------------------------------------------
-- Date Created: 05/07/2016 16:37:40
-- Generated from EDMX file: C:\Users\trailmeuser\Desktop\TraileMeServer\TrailMe\POC\MockServer\MockServer\DAL\Model\TrailMeModel.edmx
-- --------------------------------------------------

SET QUOTED_IDENTIFIER OFF;
GO
USE [TrailMeDB];
GO
IF SCHEMA_ID(N'dbo') IS NULL EXECUTE(N'CREATE SCHEMA [dbo]');
GO

-- --------------------------------------------------
-- Dropping existing FOREIGN KEY constraints
-- --------------------------------------------------

IF OBJECT_ID(N'[dbo].[FK_EventGroup]', 'F') IS NOT NULL
    ALTER TABLE [dbo].[Events] DROP CONSTRAINT [FK_EventGroup];
GO
IF OBJECT_ID(N'[dbo].[FK_UserLanguage_User]', 'F') IS NOT NULL
    ALTER TABLE [dbo].[UserLanguage] DROP CONSTRAINT [FK_UserLanguage_User];
GO
IF OBJECT_ID(N'[dbo].[FK_UserLanguage_Language]', 'F') IS NOT NULL
    ALTER TABLE [dbo].[UserLanguage] DROP CONSTRAINT [FK_UserLanguage_Language];
GO
IF OBJECT_ID(N'[dbo].[FK_UserSkill_User]', 'F') IS NOT NULL
    ALTER TABLE [dbo].[UserSkill] DROP CONSTRAINT [FK_UserSkill_User];
GO
IF OBJECT_ID(N'[dbo].[FK_UserSkill_Skill]', 'F') IS NOT NULL
    ALTER TABLE [dbo].[UserSkill] DROP CONSTRAINT [FK_UserSkill_Skill];
GO
IF OBJECT_ID(N'[dbo].[FK_UserGroup_User]', 'F') IS NOT NULL
    ALTER TABLE [dbo].[UserGroup] DROP CONSTRAINT [FK_UserGroup_User];
GO
IF OBJECT_ID(N'[dbo].[FK_UserGroup_Group]', 'F') IS NOT NULL
    ALTER TABLE [dbo].[UserGroup] DROP CONSTRAINT [FK_UserGroup_Group];
GO

-- --------------------------------------------------
-- Dropping existing tables
-- --------------------------------------------------

IF OBJECT_ID(N'[dbo].[Users]', 'U') IS NOT NULL
    DROP TABLE [dbo].[Users];
GO
IF OBJECT_ID(N'[dbo].[Groups]', 'U') IS NOT NULL
    DROP TABLE [dbo].[Groups];
GO
IF OBJECT_ID(N'[dbo].[Events]', 'U') IS NOT NULL
    DROP TABLE [dbo].[Events];
GO
IF OBJECT_ID(N'[dbo].[Languages]', 'U') IS NOT NULL
    DROP TABLE [dbo].[Languages];
GO
IF OBJECT_ID(N'[dbo].[Categories]', 'U') IS NOT NULL
    DROP TABLE [dbo].[Categories];
GO
IF OBJECT_ID(N'[dbo].[Skills]', 'U') IS NOT NULL
    DROP TABLE [dbo].[Skills];
GO
IF OBJECT_ID(N'[dbo].[UserLanguage]', 'U') IS NOT NULL
    DROP TABLE [dbo].[UserLanguage];
GO
IF OBJECT_ID(N'[dbo].[UserSkill]', 'U') IS NOT NULL
    DROP TABLE [dbo].[UserSkill];
GO
IF OBJECT_ID(N'[dbo].[UserGroup]', 'U') IS NOT NULL
    DROP TABLE [dbo].[UserGroup];
GO

-- --------------------------------------------------
-- Creating all tables
-- --------------------------------------------------

-- Creating table 'Users'
CREATE TABLE [dbo].[Users] (
    [Id] uniqueidentifier  NOT NULL,
    [FirstName] nvarchar(max)  NOT NULL,
    [LastName] nvarchar(max)  NOT NULL,
    [City] nvarchar(max)  NOT NULL,
    [MailAddress] nvarchar(max)  NOT NULL,
    [Birthdate] datetime  NOT NULL
);
GO

-- Creating table 'Groups'
CREATE TABLE [dbo].[Groups] (
    [Id] uniqueidentifier  NOT NULL,
    [Name] nvarchar(max)  NOT NULL,
    [EventId] uniqueidentifier  NOT NULL
);
GO

-- Creating table 'Events'
CREATE TABLE [dbo].[Events] (
    [Id] uniqueidentifier  NOT NULL,
    [Name] nvarchar(max)  NOT NULL,
    [TrackId] uniqueidentifier  NOT NULL,
    [Group_Id] uniqueidentifier  NOT NULL
);
GO

-- Creating table 'Languages'
CREATE TABLE [dbo].[Languages] (
    [Id] uniqueidentifier  NOT NULL,
    [Name] nvarchar(max)  NOT NULL
);
GO

-- Creating table 'Categories'
CREATE TABLE [dbo].[Categories] (
    [Id] uniqueidentifier  NOT NULL,
    [Name] nvarchar(max)  NOT NULL
);
GO

-- Creating table 'Skills'
CREATE TABLE [dbo].[Skills] (
    [Id] uniqueidentifier  NOT NULL,
    [Name] nvarchar(max)  NOT NULL
);
GO

-- Creating table 'Tracks'
CREATE TABLE [dbo].[Tracks] (
    [Id] uniqueidentifier  NOT NULL,
    [Name] nvarchar(max)  NOT NULL,
    [Zone] nvarchar(max)  NOT NULL,
    [Kilometers] int  NOT NULL,
    [Difficulty] nvarchar(max)  NOT NULL,
    [Latitude] float  NOT NULL,
    [Longitude] float  NOT NULL
);
GO

-- Creating table 'UserLanguage'
CREATE TABLE [dbo].[UserLanguage] (
    [Users_Id] uniqueidentifier  NOT NULL,
    [Languages_Id] uniqueidentifier  NOT NULL
);
GO

-- Creating table 'UserSkill'
CREATE TABLE [dbo].[UserSkill] (
    [Users_Id] uniqueidentifier  NOT NULL,
    [Skills_Id] uniqueidentifier  NOT NULL
);
GO

-- Creating table 'UserGroup'
CREATE TABLE [dbo].[UserGroup] (
    [Users_Id] uniqueidentifier  NOT NULL,
    [Groups_Id] uniqueidentifier  NOT NULL
);
GO

-- Creating table 'TrackUser'
CREATE TABLE [dbo].[TrackUser] (
    [Tracks_Id] uniqueidentifier  NOT NULL,
    [Users_Id] uniqueidentifier  NOT NULL
);
GO

-- Creating table 'TrackCategory'
CREATE TABLE [dbo].[TrackCategory] (
    [Tracks_Id] uniqueidentifier  NOT NULL,
    [Categories_Id] uniqueidentifier  NOT NULL
);
GO

-- --------------------------------------------------
-- Creating all PRIMARY KEY constraints
-- --------------------------------------------------

-- Creating primary key on [Id] in table 'Users'
ALTER TABLE [dbo].[Users]
ADD CONSTRAINT [PK_Users]
    PRIMARY KEY CLUSTERED ([Id] ASC);
GO

-- Creating primary key on [Id] in table 'Groups'
ALTER TABLE [dbo].[Groups]
ADD CONSTRAINT [PK_Groups]
    PRIMARY KEY CLUSTERED ([Id] ASC);
GO

-- Creating primary key on [Id] in table 'Events'
ALTER TABLE [dbo].[Events]
ADD CONSTRAINT [PK_Events]
    PRIMARY KEY CLUSTERED ([Id] ASC);
GO

-- Creating primary key on [Id] in table 'Languages'
ALTER TABLE [dbo].[Languages]
ADD CONSTRAINT [PK_Languages]
    PRIMARY KEY CLUSTERED ([Id] ASC);
GO

-- Creating primary key on [Id] in table 'Categories'
ALTER TABLE [dbo].[Categories]
ADD CONSTRAINT [PK_Categories]
    PRIMARY KEY CLUSTERED ([Id] ASC);
GO

-- Creating primary key on [Id] in table 'Skills'
ALTER TABLE [dbo].[Skills]
ADD CONSTRAINT [PK_Skills]
    PRIMARY KEY CLUSTERED ([Id] ASC);
GO

-- Creating primary key on [Id] in table 'Tracks'
ALTER TABLE [dbo].[Tracks]
ADD CONSTRAINT [PK_Tracks]
    PRIMARY KEY CLUSTERED ([Id] ASC);
GO

-- Creating primary key on [Users_Id], [Languages_Id] in table 'UserLanguage'
ALTER TABLE [dbo].[UserLanguage]
ADD CONSTRAINT [PK_UserLanguage]
    PRIMARY KEY CLUSTERED ([Users_Id], [Languages_Id] ASC);
GO

-- Creating primary key on [Users_Id], [Skills_Id] in table 'UserSkill'
ALTER TABLE [dbo].[UserSkill]
ADD CONSTRAINT [PK_UserSkill]
    PRIMARY KEY CLUSTERED ([Users_Id], [Skills_Id] ASC);
GO

-- Creating primary key on [Users_Id], [Groups_Id] in table 'UserGroup'
ALTER TABLE [dbo].[UserGroup]
ADD CONSTRAINT [PK_UserGroup]
    PRIMARY KEY CLUSTERED ([Users_Id], [Groups_Id] ASC);
GO

-- Creating primary key on [Tracks_Id], [Users_Id] in table 'TrackUser'
ALTER TABLE [dbo].[TrackUser]
ADD CONSTRAINT [PK_TrackUser]
    PRIMARY KEY CLUSTERED ([Tracks_Id], [Users_Id] ASC);
GO

-- Creating primary key on [Tracks_Id], [Categories_Id] in table 'TrackCategory'
ALTER TABLE [dbo].[TrackCategory]
ADD CONSTRAINT [PK_TrackCategory]
    PRIMARY KEY CLUSTERED ([Tracks_Id], [Categories_Id] ASC);
GO

-- --------------------------------------------------
-- Creating all FOREIGN KEY constraints
-- --------------------------------------------------

-- Creating foreign key on [Group_Id] in table 'Events'
ALTER TABLE [dbo].[Events]
ADD CONSTRAINT [FK_EventGroup]
    FOREIGN KEY ([Group_Id])
    REFERENCES [dbo].[Groups]
        ([Id])
    ON DELETE NO ACTION ON UPDATE NO ACTION;

-- Creating non-clustered index for FOREIGN KEY 'FK_EventGroup'
CREATE INDEX [IX_FK_EventGroup]
ON [dbo].[Events]
    ([Group_Id]);
GO

-- Creating foreign key on [Users_Id] in table 'UserLanguage'
ALTER TABLE [dbo].[UserLanguage]
ADD CONSTRAINT [FK_UserLanguage_User]
    FOREIGN KEY ([Users_Id])
    REFERENCES [dbo].[Users]
        ([Id])
    ON DELETE NO ACTION ON UPDATE NO ACTION;
GO

-- Creating foreign key on [Languages_Id] in table 'UserLanguage'
ALTER TABLE [dbo].[UserLanguage]
ADD CONSTRAINT [FK_UserLanguage_Language]
    FOREIGN KEY ([Languages_Id])
    REFERENCES [dbo].[Languages]
        ([Id])
    ON DELETE NO ACTION ON UPDATE NO ACTION;

-- Creating non-clustered index for FOREIGN KEY 'FK_UserLanguage_Language'
CREATE INDEX [IX_FK_UserLanguage_Language]
ON [dbo].[UserLanguage]
    ([Languages_Id]);
GO

-- Creating foreign key on [Users_Id] in table 'UserSkill'
ALTER TABLE [dbo].[UserSkill]
ADD CONSTRAINT [FK_UserSkill_User]
    FOREIGN KEY ([Users_Id])
    REFERENCES [dbo].[Users]
        ([Id])
    ON DELETE NO ACTION ON UPDATE NO ACTION;
GO

-- Creating foreign key on [Skills_Id] in table 'UserSkill'
ALTER TABLE [dbo].[UserSkill]
ADD CONSTRAINT [FK_UserSkill_Skill]
    FOREIGN KEY ([Skills_Id])
    REFERENCES [dbo].[Skills]
        ([Id])
    ON DELETE NO ACTION ON UPDATE NO ACTION;

-- Creating non-clustered index for FOREIGN KEY 'FK_UserSkill_Skill'
CREATE INDEX [IX_FK_UserSkill_Skill]
ON [dbo].[UserSkill]
    ([Skills_Id]);
GO

-- Creating foreign key on [Users_Id] in table 'UserGroup'
ALTER TABLE [dbo].[UserGroup]
ADD CONSTRAINT [FK_UserGroup_User]
    FOREIGN KEY ([Users_Id])
    REFERENCES [dbo].[Users]
        ([Id])
    ON DELETE NO ACTION ON UPDATE NO ACTION;
GO

-- Creating foreign key on [Groups_Id] in table 'UserGroup'
ALTER TABLE [dbo].[UserGroup]
ADD CONSTRAINT [FK_UserGroup_Group]
    FOREIGN KEY ([Groups_Id])
    REFERENCES [dbo].[Groups]
        ([Id])
    ON DELETE NO ACTION ON UPDATE NO ACTION;

-- Creating non-clustered index for FOREIGN KEY 'FK_UserGroup_Group'
CREATE INDEX [IX_FK_UserGroup_Group]
ON [dbo].[UserGroup]
    ([Groups_Id]);
GO

-- Creating foreign key on [Tracks_Id] in table 'TrackUser'
ALTER TABLE [dbo].[TrackUser]
ADD CONSTRAINT [FK_TrackUser_Track]
    FOREIGN KEY ([Tracks_Id])
    REFERENCES [dbo].[Tracks]
        ([Id])
    ON DELETE NO ACTION ON UPDATE NO ACTION;
GO

-- Creating foreign key on [Users_Id] in table 'TrackUser'
ALTER TABLE [dbo].[TrackUser]
ADD CONSTRAINT [FK_TrackUser_User]
    FOREIGN KEY ([Users_Id])
    REFERENCES [dbo].[Users]
        ([Id])
    ON DELETE NO ACTION ON UPDATE NO ACTION;

-- Creating non-clustered index for FOREIGN KEY 'FK_TrackUser_User'
CREATE INDEX [IX_FK_TrackUser_User]
ON [dbo].[TrackUser]
    ([Users_Id]);
GO

-- Creating foreign key on [Tracks_Id] in table 'TrackCategory'
ALTER TABLE [dbo].[TrackCategory]
ADD CONSTRAINT [FK_TrackCategory_Track]
    FOREIGN KEY ([Tracks_Id])
    REFERENCES [dbo].[Tracks]
        ([Id])
    ON DELETE NO ACTION ON UPDATE NO ACTION;
GO

-- Creating foreign key on [Categories_Id] in table 'TrackCategory'
ALTER TABLE [dbo].[TrackCategory]
ADD CONSTRAINT [FK_TrackCategory_Category]
    FOREIGN KEY ([Categories_Id])
    REFERENCES [dbo].[Categories]
        ([Id])
    ON DELETE NO ACTION ON UPDATE NO ACTION;

-- Creating non-clustered index for FOREIGN KEY 'FK_TrackCategory_Category'
CREATE INDEX [IX_FK_TrackCategory_Category]
ON [dbo].[TrackCategory]
    ([Categories_Id]);
GO

-- Creating foreign key on [TrackId] in table 'Events'
ALTER TABLE [dbo].[Events]
ADD CONSTRAINT [FK_TrackEvent]
    FOREIGN KEY ([TrackId])
    REFERENCES [dbo].[Tracks]
        ([Id])
    ON DELETE NO ACTION ON UPDATE NO ACTION;

-- Creating non-clustered index for FOREIGN KEY 'FK_TrackEvent'
CREATE INDEX [IX_FK_TrackEvent]
ON [dbo].[Events]
    ([TrackId]);
GO

-- --------------------------------------------------
-- Script has ended
-- --------------------------------------------------