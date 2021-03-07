DROP TABLE IF EXISTS reservation;
DROP TABLE IF EXISTS campsite_availability;
  
CREATE TABLE reservation (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  email VARCHAR(250) NOT NULL,
  first_name VARCHAR(250) NOT NULL,
  last_name VARCHAR(250) NOT NULL,
  start_date DATE NOT NULL,
  end_date DATE NOT NULL,
  reservation_status VARCHAR(250) NOT NULL
);

CREATE TABLE campsite_availability (
  id BIGINT AUTO_INCREMENT  PRIMARY KEY,
  availability_date DATE NOT NULL,
  version INT NOT NULL DEFAULT 0,
  reservation_id BIGINT, 
  FOREIGN KEY (reservation_id) REFERENCES reservation(id)
);