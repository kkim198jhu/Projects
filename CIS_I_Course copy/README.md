# Computer-Integrated Surgery

Three simulations explore how to locate a surgical tool, calibrate a robot, and align tracked measurements with a femur surface.

| Explore | What you’ll find |
| --- | --- |
| [Pointer calibration](./assignments/assignment1/README.md) | Estimate the tip position relative to a tracked marker body, then check the error at test poses. |
| [Cartesian robot calibration](./assignments/assignment2/README.md) | Fit a kinematic model to tracker observations and evaluate forward and inverse kinematics. |
| [Surface registration](./assignments/assignment3/README.md) | Use closest points and iterative closest point (ICP) registration to align pointer samples with a femur mesh and estimate target registration error. |
| [Femur mesh](./ProximalFemurMeshFile.sur) | Surface geometry for the registration problem. |
| [Shared data types](./data_types) | Represent nominal values, uncertainty, and covariance. |
