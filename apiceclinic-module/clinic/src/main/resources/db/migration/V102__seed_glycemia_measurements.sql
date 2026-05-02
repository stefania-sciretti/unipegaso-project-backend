-- V102: Seed 10 glycemia measurements with realistic clinical data

INSERT INTO glycemia_measurement (patient_id, specialist_id, measured_at, value_mg_dl, context, notes)
VALUES

-- Alessandro Russo — glicemia a digiuno nella norma (monitoraggio preventivo)
((SELECT id FROM patient WHERE fiscal_code = 'RSSALS80A01H501R'),
 (SELECT id FROM specialist WHERE email = 's.ruberti@apiceclinic.it'),
 NOW() - INTERVAL '15 days', 88, 'A_DIGIUNO',
 'Glicemia nella norma. Continuare dieta bilanciata.'),

-- Alessandro Russo — post-pasto attenzione
((SELECT id FROM patient WHERE fiscal_code = 'RSSALS80A01H501R'),
 (SELECT id FROM specialist WHERE email = 's.ruberti@apiceclinic.it'),
 NOW() - INTERVAL '14 days 10 hours', 158, 'POST_PASTO_2H',
 'Valore post-pasto leggermente elevato. Ridurre carboidrati raffinati.'),

-- Roberto Esposito — pre-diabetico, a digiuno in zona attenzione
((SELECT id FROM patient WHERE fiscal_code = 'SPTRRT75T01L219V'),
 (SELECT id FROM specialist WHERE email = 'c.maratti@apiceclinic.it'),
 NOW() - INTERVAL '10 days', 112, 'A_DIGIUNO',
 'Valore borderline. Richiesta rivalutazione metabolica tra 30 giorni.'),

-- Roberto Esposito — post-pasto elevato
((SELECT id FROM patient WHERE fiscal_code = 'SPTRRT75T01L219V'),
 (SELECT id FROM specialist WHERE email = 'c.maratti@apiceclinic.it'),
 NOW() - INTERVAL '9 days 10 hours', 207, 'POST_PASTO_2H',
 'ELEVATA. Prescritta curva glicemica. Da valutare profilo insulinico.'),

-- Marco Colombo — glicemia ottimale, controllo sportivo
((SELECT id FROM patient WHERE fiscal_code = 'CLMMRC83C15H501A'),
 (SELECT id FROM specialist WHERE email = 's.scironi@apiceclinic.it'),
 NOW() - INTERVAL '7 days', 78, 'A_DIGIUNO',
 'Atleta amatoriale. Valori eccellenti. Nessuna modifica necessaria.'),

-- Marco Colombo — post-pasto dopo pasto pre-gara
((SELECT id FROM patient WHERE fiscal_code = 'CLMMRC83C15H501A'),
 (SELECT id FROM specialist WHERE email = 's.scironi@apiceclinic.it'),
 NOW() - INTERVAL '6 days 10 hours', 125, 'POST_PASTO_2H',
 'Post-pasto carico carboidrati pre-gara. Risposta glicemica nella norma.'),

-- Giovanni Marino — iperglicemia a digiuno, anziano
((SELECT id FROM patient WHERE fiscal_code = 'MRNGNN72S08L219C'),
 (SELECT id FROM specialist WHERE email = 'c.maratti@apiceclinic.it'),
 NOW() - INTERVAL '5 days', 134, 'A_DIGIUNO',
 'ELEVATA a digiuno. Consultare medico curante per valutazione diabete tipo 2.'),

-- Matteo Bruno — giovane, controllo annuale
((SELECT id FROM patient WHERE fiscal_code = 'BRNMTT95B28F205E'),
 (SELECT id FROM specialist WHERE email = 's.ruberti@apiceclinic.it'),
 NOW() - INTERVAL '3 days', 92, 'A_DIGIUNO',
 'Controllo di routine. Valori nella norma. Piano nutrizionale adeguato.'),

-- Elena Ricci — donna, post-pasto normale
((SELECT id FROM patient WHERE fiscal_code = 'RCCLNE87C48L219V'),
 (SELECT id FROM specialist WHERE email = 'c.maratti@apiceclinic.it'),
 NOW() - INTERVAL '2 days', 96, 'A_DIGIUNO',
 'Prima misurazione. Profilo glicemico ottimale.'),

-- Sara Colombo — post-pasto in zona attenzione
((SELECT id FROM patient WHERE fiscal_code = 'CLMSRA91S65H501U'),
 (SELECT id FROM specialist WHERE email = 's.ruberti@apiceclinic.it'),
 NOW() - INTERVAL '1 day 10 hours', 172, 'POST_PASTO_2H',
 'Pasto abbondante. Consigliato controllo porzioni e attività fisica post-pasto.');
