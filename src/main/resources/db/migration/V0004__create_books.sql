CREATE TABLE books (
  isbn                VARCHAR(21)  NOT NULL PRIMARY KEY,
  book_title          VARCHAR(400) NOT NULL DEFAULT '',
  date_of_publication DATE                  DEFAULT NULL
)
