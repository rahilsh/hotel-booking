-- Create tables for hotel booking system

CREATE TABLE IF NOT EXISTS hotel (
  id INT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(255) NOT NULL,
  city VARCHAR(255) NOT NULL,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS person (
  id INT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(255) NOT NULL,
  age INT NOT NULL,
  email_id VARCHAR(255) NOT NULL UNIQUE,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS room (
  id INT AUTO_INCREMENT PRIMARY KEY,
  floor_id INT NOT NULL,
  hotel_id INT NOT NULL,
  status VARCHAR(50) NOT NULL DEFAULT 'AVAILABLE',
  version_number INT DEFAULT 0,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (hotel_id) REFERENCES hotel(id)
);

CREATE TABLE IF NOT EXISTS booking (
  id INT AUTO_INCREMENT PRIMARY KEY,
  person_id INT NOT NULL,
  room_id INT NOT NULL,
  start_time BIGINT NOT NULL,
  end_time BIGINT NOT NULL,
  status VARCHAR(50) NOT NULL DEFAULT 'BOOKED',
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (person_id) REFERENCES person(id),
  FOREIGN KEY (room_id) REFERENCES room(id)
);

-- Create indexes separately for better compatibility
CREATE INDEX IF NOT EXISTS idx_hotel ON room(hotel_id);
CREATE INDEX IF NOT EXISTS idx_room_status ON room(status);
CREATE INDEX IF NOT EXISTS idx_person ON booking(person_id);
CREATE INDEX IF NOT EXISTS idx_room ON booking(room_id);
CREATE INDEX IF NOT EXISTS idx_booking_status ON booking(status);
CREATE INDEX IF NOT EXISTS idx_time_range ON booking(start_time, end_time);
