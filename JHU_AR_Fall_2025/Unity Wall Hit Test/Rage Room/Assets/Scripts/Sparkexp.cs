using UnityEngine;

public class Sparkexp : MonoBehaviour
{
    // Start is called once before the first execution of Update after the MonoBehaviour is created
    public ParticleSystem explosion;
    void Start()
    {
        
    }

    // Update is called once per frame
    void Update()
    {
        
    }
    

    void OnCollisionEnter(Collision col)
    {
        explosion.transform.position = col.contacts[0].point;
        explosion.Play();
    }
}
