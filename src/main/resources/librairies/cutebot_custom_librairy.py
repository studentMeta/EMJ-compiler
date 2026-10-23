# Variables globales
current_orientation = 'E'
lights_on = False
siren_on = False
crazy_on = False

# Background méthodes pour la gestion des lumières, de la sirène et du crazy mode
def lights_handler():
    while True:
        if lights_on and not crazy_on:
            cuteBot.singleheadlights(cuteBot.RGBLights.RGB_L, 0, 0, 255) # Bleu
            cuteBot.singleheadlights(cuteBot.RGBLights.RGB_R, 0, 0, 255) # Bleu
            basic.pause(200)
            cuteBot.singleheadlights(cuteBot.RGBLights.RGB_L, 255, 0, 0) # Rouge
            cuteBot.singleheadlights(cuteBot.RGBLights.RGB_R, 255, 0, 0) # Rouge
            basic.pause(200)
        else:
            cuteBot.singleheadlights(cuteBot.RGBLights.RGB_L, 0, 0, 0)
            cuteBot.singleheadlights(cuteBot.RGBLights.RGB_R, 0, 0, 0)
            basic.pause(100)

def siren_handler():
    while True:
        if siren_on and not crazy_on:
            music.play_tone(Note.B5, 400) #tonalité et durée en ms
            music.play_tone(Note.E5, 400)
        else:
            basic.pause(100)


def crazy_handler():
    while True:
        if crazy_on:
            # Mouvement aléaltoires
            cuteBot.motors(randint(-50, 50), randint(-50, 50))

            # Lumières aléaltoires
            cuteBot.singleheadlights(cuteBot.RGBLights.RGB_L, randint(0, 255), randint(0, 255), randint(0, 255))
            cuteBot.singleheadlights(cuteBot.RGBLights.RGB_R, randint(0, 255), randint(0, 255), randint(0, 255))

            # Tonalités aléaltoires
            music.play_tone(randint(200, 1000), 100)

            basic.pause(150)
        else:
            basic.pause(100)

# On lance ces méthodes dès qu'on lance le programme et elles restent actives en background
control.in_background(lights_handler)
control.in_background(siren_handler)
control.in_background(crazy_handler)



# Méthodes prédéfinies du cutebot

def arrest():
    global lights_on, siren_on, crazy_on
    stop_all()
    basic.pause(500)

    # message freeze
    basic.show_string("FREEZE")

    # quelques flash de lumières blanches
    for _ in range(3):
        cuteBot.singleheadlights(cuteBot.RGBLights.RGB_L, 255, 255, 255)
        cuteBot.singleheadlights(cuteBot.RGBLights.RGB_R, 255, 255, 255)
        basic.pause(100)
        cuteBot.singleheadlights(cuteBot.RGBLights.RGB_L, 0, 0, 0)
        cuteBot.singleheadlights(cuteBot.RGBLights.RGB_R, 0, 0, 0)
        basic.pause(100)

    # musique de victoire
    music.play(music.built_in_playable_melody(Melodies.POWER_UP), music.PlaybackMode.IN_BACKGROUND)

    # message gotcha
    basic.show_string("GOTCHA")

    # symbole de la victoire
    basic.show_leds("""
        # . . . #
        # . . . #
        # . . . #
        . # . # .
        . . # . .
        """)

    # et lumières arc en ciel pour finir en beauté ^^
    rainbow_colors = [
        (255, 0, 0), # rouge
        (255, 127, 0), # orange
        (255, 255, 0), # jaune
        (0, 255, 0), # vert
        (0, 0, 255), # bleu
        (75, 0, 130), # indigo
        (148, 0, 211) # violet
    ]

    for _ in range(3):
        for color in rainbow_colors:
            r = color[0]
            g = color[1]
            b = color[2]

            cuteBot.singleheadlights(cuteBot.RGBLights.RGB_L, r, g, b)
            cuteBot.singleheadlights(cuteBot.RGBLights.RGB_R, b, r, g)
            basic.pause(150)

def toggle_lights():
    global lights_on
    lights_on = not lights_on

def toggle_siren():
    global siren_on
    siren_on = not siren_on

def stop_all():
    global lights_on, siren_on, crazy_on
    cuteBot.stopcar()
    lights_on = False
    siren_on = False
    crazy_on = False
    music.stop_all_sounds()


def crazy_mode(duration_in_seconds):
    global crazy_on
    crazy_on = True

    basic.pause(duration_in_seconds * 1000)

    # On reset l'état pour la suite du programme (en sachant qu'il y a peu de change qu'il retrouve sa position de base)
    crazy_on = False
    cuteBot.stopcar()
    cuteBot.singleheadlights(cuteBot.RGBLights.RGB_L, 0, 0, 0)
    cuteBot.singleheadlights(cuteBot.RGBLights.RGB_R, 0, 0, 0)

# Méthodes de mouvement

def move(target_orientation, cases):

    global current_orientation

    # valeurs tests à changer et trouver
    speed = 8 # en % de puissance du moteur
    time_for_one_case = 1000 # en ms

    angle = get_angle(target_orientation) - get_angle(current_orientation)
    if angle != 0:
        turn(angle)

    cuteBot.motors(speed, speed)
    basic.pause(time_for_one_case * cases)

    cuteBot.stopcar()
    basic.pause(200) # pour bien se bloquer avant de continuer

    current_orientation = target_orientation

def turn(angle):
    # valeurs tests à changer et trouver
    speed = 8 # en % de puissance du moteur
    time_for_90_turn = 1000 # en ms

    if angle == 90 or angle == -270:
        cuteBot.motors(speed, -speed)
        basic.pause(time_for_90_turn)

    elif angle == -90 or angle == 270:
        cuteBot.motors(-speed, speed)
        basic.pause(time_for_90_turn)

    elif angle == 180 or angle == -180:
        cuteBot.motors(-speed, speed)
        basic.pause(time_for_90_turn*2)

    cuteBot.stopcar()
    basic.pause(200) # pour bien se bloquer avant de continuer

# Méthode auxiliaire pour ne pas surcharger la méthode turn
def get_angle(orientation):
    if orientation == 'N':
        return 0
    elif orientation == 'E':
        return 90
    elif orientation == 'S':
        return 180
    elif orientation == 'W':
        return -90
    return 0


# Petite musique pour un touche un peu funky
music.play(music.built_in_playable_melody(Melodies.FUNK), music.PlaybackMode.UNTIL_DONE)
music.play(music.built_in_playable_melody(Melodies.FUNK), music.PlaybackMode.UNTIL_DONE)