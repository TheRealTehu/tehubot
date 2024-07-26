DROP TABLE IF EXISTS guild_statistics_data;
CREATE VIEW guild_statistics_data AS
SELECT guild_data.id,
       COALESCE(NUMBER_OF_COIN_FLIPS,0) AS NUMBER_OF_COIN_FLIPS,
       COALESCE(NUMBER_OF_DICE_ROLLS,0) AS NUMBER_OF_DICE_ROLLS
FROM guild_data
    LEFT JOIN (
        SELECT guild_id, COUNT(id) AS NUMBER_OF_COIN_FLIPS
        FROM coin_flip_data
        GROUP BY guild_id) AS coinflip
    ON guild_data.id = coinflip.guild_id
    LEFT JOIN (
        SELECT guild_id, COUNT(id) AS NUMBER_OF_DICE_ROLLS
        FROM dice_roll_data
        GROUP BY guild_id) AS diceroll
    ON coinflip.guild_id = diceroll.guild_id
GROUP BY guild_data.id;