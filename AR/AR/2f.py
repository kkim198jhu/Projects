#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Created on Fri Oct 10 03:21:17 2025

@author: kkim23
"""

### JHU INTRODUCTION TO AUGMENTED REALITY (2025) 
### Q2f. 1D Kalman Filter (Prediction-Only)

import numpy as np
import matplotlib.pyplot as plt

# Simulation parameters
dt = 0.1
num_steps = 50

# True system model
A = np.array([[1, dt],
              [0, 1]])  
B = np.array([[0.5*dt**2],
              [dt]])     
H = np.array([[1, 0]])   

# Process and measurement noise
sigma_process = 0.5
sigma_measure = 2.0
Q = sigma_process**2 * np.array([[dt**4/4, dt**3/2],
                                 [dt**3/2, dt**2]])
R = np.array([[sigma_measure**2]])

# Initialize
x_true = np.array([[0.0], [1.0]])
x_pred = np.array([[0.0], [0.0]])   # Only predictions this time
P = np.eye(2) * 1.0

# Arrays for plotting
true_positions = []
predicted_positions = []

for k in range(num_steps):
    # True system with constant acceleration
    a = 2.0
    process_noise = np.random.multivariate_normal([0.1, 0], Q).reshape(2, 1)
    x_true = A @ x_true + B * a + process_noise

    # ---- Prediction-only ----
    x_pred = A @ x_pred + B * a
    P = A @ P @ A.T + Q   # Covariance still evolves

    # Save data
    true_positions.append(x_true[0, 0])
    predicted_positions.append(x_pred[0, 0])

# Plot
t = np.arange(num_steps) * dt
plt.figure(figsize=(10, 5))
plt.plot(t, true_positions, 'g-', label='True Position', linewidth=2)
plt.plot(t, predicted_positions, 'r--', label='Prediction Only', linewidth=2)
plt.xlabel('Time [s]')
plt.ylabel('Position')
plt.title('Q2(f): Prediction-Only Kalman Filter (No Measurement Updates)')
plt.legend()
plt.grid(True)
plt.show()
