-- V102 H2: Seed 10 glycemia measurements with realistic clinical data

INSERT INTO glycemia_measurement (patient_id, specialist_id, measured_at, value_mg_dl, context, notes)
VALUES

((SELECT id FROM patient WHERE fiscal_code = 'RSSALS80A01H501R'),
 (SELECT id FROM specialist WHERE email = 's.ruberti@apiceclinic.it'),
 DATEADD('DAY', -15, NOW()), 88, 'A_DIGIUNO',
 'Glicemia nella norma. Continuare dieta bilanciata.'),

((SELECT id FROM patient WHERE fiscal_code = 'RSSALS80A01H501R'),
 (SELECT id FROM specialist WHERE email = 's.ruberti@apiceclinic.it'),
 DATEADD('HOUR', -346, NOW()), 158, 'POST_PASTO_2H',
 'Valore post-pasto leggermente elevato. Ridurre carboidrati raffinati.'),

((SELECT id FROM patient WHERE fiscal_code = 'SPTRRT75T01L219V'),
 (SELECT id FROM specialist WHERE email = 'c.maratti@apiceclinic.it'),
 DATEADD('DAY', -10, NOW()), 112, 'A_DIGIUNO',
 'Valore borderline. Richiesta rivalutazione metabolica tra 30 giorni.'),

((SELECT id FROM patient WHERE fiscal_code = 'SPTRRT75T01L219V'),
 (SELECT id FROM specialist WHERE email = 'c.maratti@apiceclinic.it'),
 DATEADD('HOUR', -226, NOW()), 207, 'POST_PASTO_2H',
 'ELEVATA. Prescritta curva glicemica. Da valutare profilo insulinico.'),

((SELECT id FROM patient WHERE fiscal_code = 'CLMMRC83C15H501A'),
 (SELECT id FROM specialist WHERE email = 's.scironi@apiceclinic.it'),
 DATEADD('DAY', -7, NOW()), 78, 'A_DIGIUNO',
 'Atleta amatoriale. Valori eccellenti. Nessuna modifica necessaria.'),

((SELECT id FROM patient WHERE fiscal_code = 'CLMMRC83C15H501A'),
 (SELECT id FROM specialist WHERE email = 's.scironi@apiceclinic.it'),
 DATEADD('HOUR', -154, NOW()), 125, 'POST_PASTO_2H',
 'Post-pasto carico carboidrati pre-gara. Risposta glicemica nella norma.'),

((SELECT id FROM patient WHERE fiscal_code = 'MRNGNN72S08L219C'),
 (SELECT id FROM specialist WHERE email = 'c.maratti@apiceclinic.it'),
 DATEADD('DAY', -5, NOW()), 134, 'A_DIGIUNO',
 'ELEVATA a digiuno. Consultare medico curante per valutazione diabete tipo 2.'),

((SELECT id FROM patient WHERE fiscal_code = 'BRNMTT95B28F205E'),
 (SELECT id FROM specialist WHERE email = 's.ruberti@apiceclinic.it'),
 DATEADD('DAY', -3, NOW()), 92, 'A_DIGIUNO',
 'Controllo di routine. Valori nella norma. Piano nutrizionale adeguato.'),

((SELECT id FROM patient WHERE fiscal_code = 'RCCLNE87C48L219V'),
 (SELECT id FROM specialist WHERE email = 'c.maratti@apiceclinic.it'),
 DATEADD('DAY', -2, NOW()), 96, 'A_DIGIUNO',
 'Prima misurazione. Profilo glicemico ottimale.'),

((SELECT id FROM patient WHERE fiscal_code = 'CLMSRA91S65H501U'),
 (SELECT id FROM specialist WHERE email = 's.ruberti@apiceclinic.it'),
 DATEADD('HOUR', -34, NOW()), 172, 'POST_PASTO_2H',
 'Pasto abbondante. Consigliato controllo porzioni e attivita fisica post-pasto.');
