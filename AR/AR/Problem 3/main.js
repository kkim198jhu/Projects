const canvas = document.getElementById("renderCanvas");
const engine = new BABYLON.Engine(canvas, true);

const createScene = async () => {
    const scene = new BABYLON.Scene(engine);

    // Light
    const light = new BABYLON.HemisphericLight("light", new BABYLON.Vector3(0,1,0), scene);

    // WebXR AR experience
    const xr = await scene.createDefaultXRExperienceAsync({
        uiOptions: { sessionMode: "immersive-ar" },
        optionalFeatures: true
    });

    // Load the model
    BABYLON.SceneLoader.ImportMesh(
        "", 
        "models/", 
        "model.glb", 
        scene, 
        function(meshes, particleSystems, skeletons, animationGroups) {

            console.log("Meshes:", meshes);
            console.log("Skeletons:", skeletons);
            console.log("Animations:", animationGroups);

            const modelMesh = meshes[0]; // main mesh with morph targets
            const morphManager = modelMesh.morphTargetManager;

            const facial1 = morphManager.getTarget(0); // shape key 1
            const facial2 = morphManager.getTarget(1); // shape key 2
            const walk = animationGroups.length > 0 ? animationGroups[0] : null; // skeleton animation

            // Start with walking
            if(walk) walk.start(true);

            let state = "walking";

            xr.input.onControllerAddedObservable.add((controller) => {
                controller.onMotionControllerInitObservable.add((motionController) => {
                    const trigger = motionController.getComponent("xr-standard-trigger");
                    if(trigger){
                        trigger.onButtonStateChangedObservable.add(() => {
                            // Cycle states: walking -> facial1 -> facial2 -> walking
                            if(state === "walking"){
                                if(walk) walk.stop();
                                facial1.influence = 1;
                                facial2.influence = 0;
                                state = "facial1";
                            } else if(state === "facial1"){
                                facial1.influence = 0;
                                facial2.influence = 1;
                                state = "facial2";
                            } else {
                                facial2.influence = 0;
                                if(walk) walk.start(true);
                                state = "walking";
                            }
                        });
                    }
                });
            });
        }
    );

    return scene;
};

createScene().then(scene => {
    engine.runRenderLoop(() => scene.render());
});

window.addEventListener("resize", () => engine.resize());
