DROP TABLE IF EXISTS guild_statistics_data;
CREATE VIEW guild_statistics_data AS
SELECT guild_data.id,
       COALESCE(NUMBER_OF_COIN_FLIPS, 0)    AS NUMBER_OF_COIN_FLIPS,
       COALESCE(NUMBER_OF_DICE_ROLLS, 0)    AS NUMBER_OF_DICE_ROLLS,
       COALESCE(NUMBER_OF_WIKIS_CHECKED, 0) AS NUMBER_OF_WIKIS_CHECKED,
       COALESCE(NUMBER_OF_GIFS_SENT, 0)     AS NUMBER_OF_GIFS_SENT,
       COALESCE(NUMBER_OF_POLLS, 0)         AS NUMBER_OF_POLLS
FROM guild_data
         LEFT JOIN (SELECT guild_id, COUNT(id) AS NUMBER_OF_COIN_FLIPS
                    FROM coin_flip_data
                    GROUP BY guild_id) AS coinflip
                   ON guild_data.id = coinflip.guild_id
         LEFT JOIN (SELECT guild_id, COUNT(id) AS NUMBER_OF_DICE_ROLLS
                    FROM dice_roll_data
                    GROUP BY guild_id) AS diceroll
                   ON guild_data.id = diceroll.guild_id
         LEFT JOIN (SELECT guild_id, COUNT(id) AS NUMBER_OF_WIKIS_CHECKED
                    FROM get_wiki_data
                    GROUP BY guild_id) AS wikisearch
                   ON guild_data.id = wikisearch.guild_id
         LEFT JOIN (SELECT guild_id, COUNT(id) AS NUMBER_OF_GIFS_SENT
                    FROM send_gif_data
                    GROUP BY guild_id) AS gifsent
                   ON guild_data.id = gifsent.guild_id
         LEFT JOIN (SELECT guild_id, COUNT(id) AS NUMBER_OF_POLLS
                    FROM poll_data
                    GROUP BY guild_id) AS poll
                   ON guild_data.id = poll.guild_id;