-- V103: Seed 10 training plans with real patients and specialists

INSERT INTO training_plan (patient_id, trainer_id, title, description, weeks, sessions_per_week, active)
VALUES

-- Luca Ricci (giovane, 34 anni) — PT Luca Siretta
((SELECT id FROM patient WHERE fiscal_code = 'RCCLCU90L22F205B'),
 (SELECT id FROM specialist WHERE email = 'l.siretta@apiceclinic.it'),
 'Ipertrofia muscolare avanzata',
 'Piano di allenamento per la crescita muscolare. Suddivisione push/pull/legs con esercizi composti (squat, stacchi, panca). Progressione lineare del carico ogni settimana.',
 12, 5, true),

-- Marco Colombo (atleta amatoriale, 43 anni) — PT Luca Siretta
((SELECT id FROM patient WHERE fiscal_code = 'CLMMRC83C15H501A'),
 (SELECT id FROM specialist WHERE email = 'l.siretta@apiceclinic.it'),
 'Preparazione atletica running',
 'Piano misto forza/endurance per corridore amatoriale. Sessioni di interval training, lunghi lenti e lavoro di forza funzionale per prevenire infortuni.',
 8, 4, true),

-- Alessandro Russo (46 anni, sovrappeso) — PT Luca Siretta
((SELECT id FROM patient WHERE fiscal_code = 'RSSALS80A01H501R'),
 (SELECT id FROM specialist WHERE email = 'l.siretta@apiceclinic.it'),
 'Dimagrimento e tonificazione',
 'Allenamento metabolico ad alta intensità (HIIT) abbinato a circuit training. Obiettivo: riduzione massa grassa e miglioramento capacità cardiovascolare.',
 16, 3, true),

-- Matteo Bruno (31 anni, principiante) — PT Luca Siretta
((SELECT id FROM patient WHERE fiscal_code = 'BRNMTT95B28F205E'),
 (SELECT id FROM specialist WHERE email = 'l.siretta@apiceclinic.it'),
 'Full body principiante',
 'Introduzione all allenamento con i pesi. Esercizi a corpo libero e macchine isotoniche. Focus sulla tecnica esecutiva e sulla corretta respirazione.',
 6, 3, true),

-- Giovanni Marino (53 anni, sedentario) — Fisioterapista Michele Lavori
((SELECT id FROM patient WHERE fiscal_code = 'MRNGNN72S08L219C'),
 (SELECT id FROM specialist WHERE email = 'm.lavori@apiceclinic.it'),
 'Mobilità e postura over 50',
 'Programma di mobilità articolare, stretching dinamico e rinforzo del core per contrastare gli effetti del lavoro sedentario. Bassa intensità, alta frequenza.',
 10, 4, true),

-- Lorenzo Gallo (38 anni, dolore lombare) — Fisioterapista Michele Lavori
((SELECT id FROM patient WHERE fiscal_code = 'GLLLRN87M14L219F'),
 (SELECT id FROM specialist WHERE email = 'm.lavori@apiceclinic.it'),
 'Riabilitazione lombare',
 'Piano di riabilitazione per lombosciatalgia cronica. Esercizi di stabilizzazione lombare, rinforzo muscoli paravertebrali e core stability. No carichi assiali.',
 8, 3, true),

-- Davide Romano (37 anni) — PT Luca Siretta
((SELECT id FROM patient WHERE fiscal_code = 'RMNDVD88P01G273X'),
 (SELECT id FROM specialist WHERE email = 'l.siretta@apiceclinic.it'),
 'Forza e potenza sport',
 'Allenamento di forza massimale e potenza esplosiva per sport di squadra. Lavoro con bilanciere olimpico, pliometria e sprint. Periodizzazione ondulata.',
 10, 4, true),

-- Sara Colombo (donna, 35 anni) — PT Luca Siretta
((SELECT id FROM patient WHERE fiscal_code = 'CLMSRA91S65H501U'),
 (SELECT id FROM specialist WHERE email = 'l.siretta@apiceclinic.it'),
 'Tonificazione femminile',
 'Piano orientato alla definizione muscolare. Lavoro su glutei, addome e parte superiore. Combinazione di esercizi a corpo libero, bande elastiche e pesi leggeri.',
 12, 4, true),

-- Roberto Esposito (51 anni, recupero post infortunio) — Fisioterapista Michele Lavori
((SELECT id FROM patient WHERE fiscal_code = 'SPTRRT75T01L219V'),
 (SELECT id FROM specialist WHERE email = 'm.lavori@apiceclinic.it'),
 'Recupero post-operatorio ginocchio',
 'Protocollo riabilitativo post artroscopia al ginocchio sinistro. Fase 1: riduzione edema e recupero ROM. Fase 2: rinforzo quadricipite e ischio-crurali. Fase 3: propriocezione.',
 14, 3, false),

-- Stefano Conti (48 anni, prevenzione cardiovascolare) — PT Luca Siretta
((SELECT id FROM patient WHERE fiscal_code = 'CNTSFN78D03H501G'),
 (SELECT id FROM specialist WHERE email = 'l.siretta@apiceclinic.it'),
 'Cardio e prevenzione metabolica',
 'Piano di allenamento aerobico progressivo per la prevenzione cardiovascolare. Lavoro in zona 2 (60-70% FC max), camminate veloci, cyclette e lavoro funzionale leggero.',
 20, 3, true);
