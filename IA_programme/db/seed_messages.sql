-- Improved, human-sounding motivational messages
-- Run this after creating schemas/tables. It will replace the message content.

USE daily_log;
SET FOREIGN_KEY_CHECKS=0;
TRUNCATE mood_very_good;
TRUNCATE mood_neutral;
TRUNCATE mood_poor;
TRUNCATE anxiety_very_good;
TRUNCATE anxiety_neutral;
TRUNCATE anxiety_poor;
TRUNCATE physical_very_good;
TRUNCATE physical_neutral;
TRUNCATE physical_poor;
SET FOREIGN_KEY_CHECKS=1;

-- Mood: very good
INSERT INTO mood_very_good (message) VALUES
('You showed up with light this week. Keep some for yourself too.'),
('That spark you feel? It''s earned. Enjoy it.'),
('You made joy look effortless—celebrate the tiny choices behind it.'),
('Momentum is on your side. Ride it at a pace that feels kind.'),
('You brought warmth into the room. Keep choosing what fuels it.'),
('Your mood lifted others—don''t forget to lift yourself.'),
('You found ways to feel good. Note them. Repeat them.'),
('Great energy this week. Anchor it with rest and routine.'),
('Your smile said a lot—your effort said more.'),
('You didn''t just have a good week—you built it.'),
('Your optimism was practical, not pretend. That matters.'),
('Keep the bar human: progress over perfection.'),
('Confidence looks good on you because it''s grounded.'),
('Keep what worked. Thank what didn''t for the lesson.'),
('You earned peace without rushing the process.'),
('This felt like you. Hold onto that version.'),
('You made room for good things—and they showed up.'),
('Let this week be a reminder: you can create better days.'),
('Bright doesn''t mean loud. Your calm confidence is enough.'),
('Bottle the habits that got you here. Uncap when needed.');

-- Mood: neutral
INSERT INTO mood_neutral (message) VALUES
('A steady week still moves you forward.'),
('Neutral isn''t nothing—it''s balance. Build from here.'),
('You kept your footing. That''s real progress.'),
('Quiet weeks plant loud wins.'),
('Not every day shines. Showing up still counts.'),
('You held the middle—now pick one small thing to lift.'),
('Your baseline is getting stronger.'),
('Calm effort is still effort.'),
('You navigated without drama. That''s a win.'),
('Keep the routines that kept you steady.'),
('Notice the small bright spots—they add up.'),
('Your self-talk shaped the tone. Keep it kind.'),
('Neutral gives you room to choose. Choose gently.'),
('You didn''t force it. Wise.'),
('Today''s 1% still counts tomorrow.'),
('Keep the door open for better without chasing it.'),
('You''re allowed to pace yourself.'),
('Balance is a skill you''re learning.'),
('A quiet reset is still momentum.'),
('You did enough. Now rest well.');

-- Mood: poor
INSERT INTO mood_poor (message) VALUES
('Hard week. You are not your feelings.'),
('Be on your side today. Start small.'),
('You don''t have to do it all—just the next kind thing.'),
('Low energy isn''t failure—it''s information.'),
('You mattered on your hardest day too.'),
('Let “okay” be enough while you heal.'),
('You''ve survived 100% of your worst days.'),
('Ask for help. It''s strength, not weakness.'),
('Breathe. Name five things you can see. You''re here.'),
('One gentle action: water, stretch, sunlight.'),
('You can try again without starting over.'),
('Rest is productive when you need it.'),
('Your worth is not up for debate.'),
('This chapter isn''t the whole book.'),
('If your body says stop, listen.'),
('You''re allowed to move slowly.'),
('Set the bar to “possible” today.'),
('Feelings pass. You remain.'),
('Being human is messy. You''re allowed.'),
('Try the 5-minute rule: begin, then decide.');

-- Anxiety: very good
INSERT INTO anxiety_very_good (message) VALUES
('You stayed grounded. Keep the habits that helped.'),
('Your calm was a choice and a practice.'),
('You made space between feeling and reacting. Powerful.'),
('Breath, boundaries, breaks—nice trio.'),
('You redirected worry into action. That works.'),
('Your nervous system got what it needed.'),
('You trusted yourself. Keep reinforcing that.'),
('Less noise, more clarity. Keep going.'),
('You moved at your pace and it paid off.'),
('Calm isn''t luck—it''s your skill growing.'),
('You kept perspective when it tried to shrink.'),
('You honored your limits and found ease.'),
('Keep choosing the environments that settle you.'),
('Your tools worked—note which ones.'),
('You responded with care, not fear.'),
('You met stress early and kindly.'),
('Your body believed you when you said “we''re safe.”'),
('Keep protecting your peace—gently.'),
('You made peace practical.'),
('Let this calm be the new reference point.');

-- Anxiety: neutral
INSERT INTO anxiety_neutral (message) VALUES
('Manageable moments handled well.'),
('You paused before spiraling—nice catch.'),
('You used tools, not just willpower.'),
('Name it, breathe, move—keep that loop.'),
('You carried discomfort without letting it drive.'),
('You asked “what helps?” and listened.'),
('Small worries, steady you.'),
('You didn''t feed the thoughts. That matters.'),
('Anchor back into your body—it works.'),
('You took breaks without guilt.'),
('You chose facts over fears, often enough.'),
('You noticed early signs and softened.'),
('Keep your supports visible: water, walk, window, words.'),
('You''re learning when to push and when to pause.'),
('Neutral is stable. Build safety from here.'),
('Your pace was sustainable.'),
('You practiced self-talk that soothed.'),
('Keep mornings gentle; nights honest.'),
('You''re allowed to not engage every thought.'),
('You did fine. Keep it simple.');

-- Anxiety: poor
INSERT INTO anxiety_poor (message) VALUES
('Big feelings showed up. You stayed.'),
('If today is heavy, set it down often.'),
('You don''t have to earn your breath. Take it.'),
('Ground: 5 things you see, 4 touch, 3 hear, 2 smell, 1 taste.'),
('Ask for help before it feels “bad enough.”'),
('Your body is trying to protect you. Thank it, guide it.'),
('Short steps count: shower, snack, sunlight.'),
('You can be scared and capable.'),
('Let the wave pass without chasing it.'),
('Cancel something. Your peace matters.'),
('Stretch your timeline, not your standards.'),
('Speak to yourself like someone you love.'),
('You are safe enough right now.'),
('Choose one anchor: music, walk, message a friend.'),
('It''s okay to slow the day way down.'),
('Nothing is wrong with you for feeling this.'),
('Rest your shoulders. Unclench your jaw.'),
('Try again tomorrow; today can be light.'),
('You''re not behind—you''re human.'),
('Let “good enough” be the goal.');

-- Physical: very good
INSERT INTO physical_very_good (message) VALUES
('Your body felt supported—keep what worked.'),
('Sleep, movement, fuel—it showed.'),
('You listened to your limits and got stronger.'),
('Consistency beat intensity. Nicely done.'),
('You trained recovery as much as effort.'),
('Hydration and sunlight did their job.'),
('Your routine looked sustainable.'),
('You moved with intention, not punishment.'),
('Stronger doesn''t need to be louder.'),
('Your energy matched your choices.'),
('You cared for your future self this week.'),
('You built capacity gently.'),
('Form, food, and rest—great balance.'),
('You finished sessions with something left. Smart.'),
('You proved that small daily reps matter.'),
('Your body said “thank you.” You heard it.'),
('Keep the warm-up, keep the cool-down, keep the wins.'),
('Movement looked like kindness.'),
('You moved often and recovered well.'),
('This is a foundation you can stand on.');

-- Physical: neutral
INSERT INTO physical_neutral (message) VALUES
('Maintenance weeks matter.'),
('You kept the basics: water, walks, windows.'),
('Neutral energy is still usable energy.'),
('Stretch what you have, gently.'),
('You showed up enough. That''s enough.'),
('Make “better than nothing” your friend.'),
('Routines carry you when motivation doesn''t.'),
('A few good meals go a long way.'),
('Light movement still counts.'),
('Sleep is training—keep protecting it.'),
('Your body appreciates predictability.'),
('Small upgrades add up: one fruit, one stretch.'),
('You paced the week to your capacity.'),
('Check in: water? breath? posture?'),
('Neutral weeks help you reset.'),
('You can start again any morning.'),
('Keep it simple and repeatable.'),
('Fuel before you fade.'),
('Your baseline is slowly rising.'),
('Listen, adjust, carry on.');

-- Physical: poor
INSERT INTO physical_poor (message) VALUES
('Rest isn''t quitting. It''s repair.'),
('Your body asked for care. Answer softly.'),
('Low energy needs gentleness, not judgment.'),
('Feed, water, move a little. Then reassess.'),
('Pain is information; respond, don''t punish.'),
('Short walks, long exhales.'),
('Today''s win can be a nap.'),
('Your worth isn''t tied to output.'),
('A slow day can still be a kind day.'),
('Reduce the plan until it''s doable.'),
('Ask for support with practical things.'),
('If you can''t do more, do less on purpose.'),
('Your body is on your team.'),
('Recovery seasons are part of the story.'),
('Fuel first; motivation follows.'),
('Gentle movement beats none.'),
('Pick one: hydrate, stretch, sunlight.'),
('Treat yourself like someone healing.'),
('Start again tomorrow. You''re okay.'),
('Small care, big difference.');
