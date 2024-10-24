CREATE TABLE booking (
    id SERIAL PRIMARY KEY,
    meeting_room_id BIGINT NOT NULL,
    employee_id BIGINT NOT NULL,
    date DATE NOT NULL,
    time_from TIME NOT NULL,
    time_to TIME NOT NULL,
    FOREIGN KEY (meeting_room_id) REFERENCES meeting_room(id),
    FOREIGN KEY (employee_id) REFERENCES employee(id)
);



INSERT INTO booking (meeting_room_id, employee_id, date, time_from, time_to)
VALUES (1, 1, '2024-10-24', '09:00', '11:00'); -- Conference Room A booked by Alice

INSERT INTO booking (meeting_room_id, employee_id, date, time_from, time_to)
VALUES (1, 1, '2024-10-24', '13:00', '14:00');

INSERT INTO booking (meeting_room_id, employee_id, date, time_from, time_to)
VALUES (1, 2, '2024-10-24', '14:00', '16:00');

INSERT INTO booking (meeting_room_id, employee_id, date, time_from, time_to)
VALUES (1, 2, '2024-10-24', '16:00', '19:00'); -- Conference Room B booked by Bob

INSERT INTO booking (meeting_room_id, employee_id, date, time_from, time_to)
VALUES (3, 3, '2024-10-25', '13:00', '14:00'); -- Executive Suite booked by Charlie

INSERT INTO booking (meeting_room_id, employee_id, date, time_from, time_to)
VALUES (4, 4, '2024-10-26', '14:00', '16:00'); -- Board Room booked by Diana

INSERT INTO booking (meeting_room_id, employee_id, date, time_from, time_to)
VALUES (5, 5, '2024-10-26', '15:00', '17:00'); -- Training Room booked by Eve
