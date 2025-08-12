-- Seed testing data for the past two weeks
-- This script upserts 14 days of entries ending today.

CREATE SCHEMA IF NOT EXISTS daily_log;
USE daily_log;

INSERT INTO daily_entries (
  entry_date,
  mood_scale,
  anxiety_scale,
  physical_scale,
  overview,
  todo,
  goals,
  gratitude
) VALUES
  (CURDATE() - INTERVAL 13 DAY, 3, 3, 4,
   'Kept things steady. Short walk after school.',
   '[ ] Math revision\n[x] Tidy desk',
   '3 focused study blocks, 1 stretch break each.',
   'Warm sunlight after lunch.'),

  (CURDATE() - INTERVAL 12 DAY, 4, 2, 4,
   'Good mood overall, worked through tasks calmly.',
   '[x] English reading\n[ ] Prep questions',
   'Finish reading notes before dinner.',
   'A kind message from a friend.'),

  (CURDATE() - INTERVAL 11 DAY, 2, 4, 3,
   'Low energy morning, picked up late afternoon.',
   '[ ] Flashcards\n[x] Short walk',
   'Just begin, even if small.',
   'Hot tea and a quiet hour.'),

  (CURDATE() - INTERVAL 10 DAY, 3, 3, 3,
   'Balanced day. Not perfect, still progress.',
   '[ ] Physics recap\n[x] Room reset',
   'Review two tricky topics slowly.',
   'Dinner with family.'),

  (CURDATE() - INTERVAL 9 DAY, 5, 1, 4,
   'Felt upbeat and present most of the day.',
   '[x] Practice problems\n[ ] Pack bag',
   'Keep momentum, keep it kind.',
   'A laugh that lasted.'),

  (CURDATE() - INTERVAL 8 DAY, 3, 2, 5,
   'Decent focus, body felt good after stretching.',
   '[ ] Biology notes\n[x] Stretch routine',
   'Protect sleep window tonight.',
   'Fresh air and a clear sky.'),

  (CURDATE() - INTERVAL 7 DAY, 4, 2, 4,
   'Quiet confidence. Kept routines simple.',
   '[x] Plan week\n[ ] Pack lunch',
   'Three small wins beat one big one.',
   'Making time for breakfast.'),

  (CURDATE() - INTERVAL 6 DAY, 2, 4, 2,
   'Tense morning. Took breaks to reset.',
   '[ ] Review notes\n[x] Hydrate often',
   'Reduce the plan until it feels doable.',
   'A supportive check‑in.'),

  (CURDATE() - INTERVAL 5 DAY, 3, 3, 3,
   'Handled the essentials. That counts.',
   '[ ] Practice set\n[x] Laundry',
   'Start early, stop on time.',
   'A good song on repeat.'),

  (CURDATE() - INTERVAL 4 DAY, 4, 2, 4,
   'Clear head, steady pace.',
   '[x] Review summary\n[ ] Prep flashcards',
   'Protect focus: phone off for an hour.',
   'A quiet walk at sunset.'),

  (CURDATE() - INTERVAL 3 DAY, 3, 3, 3,
   'Average day with a few bright moments.',
   '[ ] Past paper Qs\n[x] Room tidy',
   'One step before dinner, one after.',
   'Seeing progress on a hard topic.'),

  (CURDATE() - INTERVAL 2 DAY, 2, 4, 2,
   'Overwhelmed at times. Chose smaller steps.',
   '[ ] Two problems max\n[x] Stretch 5 min',
   'Slow is still forward.',
   'A message that made me smile.'),

  (CURDATE() - INTERVAL 1 DAY, 4, 2, 4,
   'Good flow in the afternoon. Finished priorities.',
   '[x] Biology recap\n[ ] Bag for tomorrow',
   'Keep the 25/5 focus blocks.',
   'Warm shower and early bed.'),

  (CURDATE(), 3, 2, 3,
   'Solid baseline. Saving energy for tomorrow.',
   '[ ] Light revision\n[x] Prep snack',
   'Be kind to future me: set clothes out.',
   'Grateful for a calm evening.')
AS new
ON DUPLICATE KEY UPDATE
  mood_scale     = new.mood_scale,
  anxiety_scale  = new.anxiety_scale,
  physical_scale = new.physical_scale,
  overview       = new.overview,
  todo           = new.todo,
  goals          = new.goals,
  gratitude      = new.gratitude;
