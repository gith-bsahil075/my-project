"""
Typewriter Lyrics Animation
----------------------------
Prints lyrics character-by-character like a typewriter, with per-line
and per-character timing so the animation can match a song's rhythm.

Concepts used:
- Lists & loops      -> lyrics stored as list of (line, speed) tuples
- Functions          -> reusable typewriter() and play_song() functions
- time.sleep()       -> controls character/line timing
- sys.stdout.write() -> prints without adding newline each time
- sys.stdout.flush() -> forces the character onto the terminal instantly
"""

import sys
import time
import random

# ---------------------------------------------------------
# 1. LYRICS DATA
# Each item: (line_text, char_delay, pause_after_line)
# char_delay -> speed between letters (smaller = faster/energetic)
# pause_after_line -> silence/breath before next line (like a beat drop)
# ---------------------------------------------------------
LYRICS = [
    ("Uska hoon, usmein hoon, usee hoon, usi ka rehne de", 0.05, 0.8),
    ("Main toh pyaasa hoon, hai dariya, woh zariya, woh jeene ka mere", 0.05, 0.8),
    ("Mujhe ghar de, gali de, shehar de, usi ke naam ke", 0.04, 0.7),
    ("Kadam ye chale ya ruke, ab usi ke vaaste", 0.04, 0.9),
    ("", 0.0, 0.5),

    ("Dil mujhe de, agar dard de uska par", 0.05, 0.8),
    ("Uski ho woh hansi, goonje jo mera ghar", 0.05, 1.0),
    ("Ae Khuda, Ae Khuda", 0.08, 0.8),
    ("Jab bana, uska hi bana", 0.05, 0.8),
    ("Ae Khuda, Ae Khuda", 0.08, 0.8),
    ("Jab bana, uska hi bana", 0.05, 1.2),
]

def typewriter(line: str, char_delay: float, jitter: float = 0.01) -> None:
    """
    Print a single line character-by-character.
    A tiny random 'jitter' is added/subtracted from char_delay so the
    animation feels more human/musical instead of robotic.
    """
    for ch in line:
        sys.stdout.write(ch)
        sys.stdout.flush()

        # extra pause on punctuation, like a natural breath in singing
        extra = 0.25 if ch in ",.!?-" else 0.0

        delay = char_delay + random.uniform(-jitter, jitter) + extra
        time.sleep(max(delay, 0))

    print()  # move to next line after the full line is typed


def play_song(lyrics: list) -> None:
    """Loop through every (line, speed, pause) tuple and animate it."""
    for line, char_delay, pause_after_line in lyrics:
        typewriter(line, char_delay)
        time.sleep(pause_after_line)


if __name__ == "__main__":
    print("=" * 40)
    print(" Now Playing: Code & Rhythm (typed live)")
    print("=" * 40, "\n")
    time.sleep(1)

    play_song(LYRICS)

    print("\n" + "=" * 40)
    print(" End of track.")
