local stream= KEYS[1]
local group= ARGV[1]
local consumer= ARGV[2]
local timeout = ARGV[3]

local pending = redis.call('XPENDING', stream, group)