INSERT INTO training_types (id, training_type_name) VALUES (1, 'CROSSFIT') ON CONFLICT (id) DO NOTHING;
INSERT INTO training_types (id, training_type_name) VALUES (2, 'ZUMBA') ON CONFLICT (id) DO NOTHING;
INSERT INTO training_types (id, training_type_name) VALUES (3, 'FUNCTIONAL') ON CONFLICT (id) DO NOTHING;
INSERT INTO training_types (id, training_type_name) VALUES (4, 'BOXING') ON CONFLICT (id) DO NOTHING;
INSERT INTO training_types (id, training_type_name) VALUES (5, 'PILATES') ON CONFLICT (id) DO NOTHING;
INSERT INTO training_types (id, training_type_name) VALUES (6, 'BOULDERING') ON CONFLICT (id) DO NOTHING;