
class BasicBuilder(object):
    def __init__(self):
        pass
        
    def encode(self, name, group, msg, time):
        # TODO encode message
        payload = (f"{group},{name},{msg},{time}")
        return (f"{len(payload):04d},{payload}")

    def decode(self, raw):
        # TODO complete parsing
        parts = raw.split(",", 5)
        if len(parts) != 5:
            raise ValueError(f"message format error: {raw}")
        else:
            return parts[2],parts[1],parts[3],parts[4]
