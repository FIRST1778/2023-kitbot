#!/usr/bin/env python3
# See Constants.kt.  Math for MK4i NEO swerve modules.
# https://www.swervedrivespecialties.com/products/mk4i-swerve-module

from math import sqrt, pi
from code import interact

def inches(n): return n * 0.0254  # inch to meters
def rpm(n): return n * pi / 30    # rpm to rad/s

module_xy = inches(11.75)
module_radius = module_xy * sqrt(2)
drive_reduction = (14 / 50) * (27 / 17) * (15 / 45)
colson_wheel_radius = inches(2)
neo_free_speed = rpm(5676)
max_speed = neo_free_speed * drive_reduction * colson_wheel_radius
max_angular_speed = max_speed / module_radius
