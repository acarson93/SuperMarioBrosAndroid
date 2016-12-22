package com.sc.game;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;

/**
 * Created by AC on 12/19/2016.
 */
public class WorldContactListen implements ContactListener {


    @Override
    public void beginContact(Contact contact) {

        Fixture a = contact.getFixtureA();
        Fixture b = contact.getFixtureB();
        //int cd = a.getFilterData().categoryBits | b.getFilterData().categoryBits;

        //System.out.println("a is " + a.getFilterData().categoryBits);
        //System.out.println("b is " + b.getFilterData().categoryBits);
        boolean isBig = false;


        if(a.getUserData() == "head" || b.getUserData() == "head"){

            Fixture head;
            Fixture obj;



            if(a.getUserData()=="head"){
                head = a;
                obj = b;


            }

            else{
                head = b;
                obj = a;

            }




            if(obj.getUserData() != null && InteractiveTileObjects.class.isAssignableFrom(obj.getUserData().getClass())){

                if(obj.getUserData() instanceof Coin){
                    ((InteractiveTileObjects) obj.getUserData()).onHeadHit();
                }
                if(Mario.hG) {
                    ((InteractiveTileObjects) obj.getUserData()).onHeadHit();
                }
            }

            //System.out.println("i made it here");



            /*switch (cd){


                case MarioBros.ENEMY_HEAD_BIT | MarioBros.MARIO_BIT:

                    System.out.println("inside case");
                    if(a.getFilterData().categoryBits == MarioBros.ENEMY_HEAD_BIT)
                        ((Enemies)a.getUserData()).headJump();
                    else
                        ((Enemies)b.getUserData()).headJump();
                    break;

            }*/
        }

        if(a.getFilterData().categoryBits == MarioBros.ENEMY_BIT && b.getFilterData().categoryBits == MarioBros.MARIO_BIT){

            System.out.println("im in a/b");
            ((Mario)b.getUserData()).gotHit();

        }

        if(b.getFilterData().categoryBits == MarioBros.ENEMY_BIT && a.getFilterData().categoryBits == MarioBros.MARIO_BIT){
            //System.out.println("b is " + b.getFilterData().categoryBits);


            ((Mario)a.getUserData()).gotHit();

        }

        if(b.getFilterData().categoryBits == MarioBros.ENEMY_HEAD_BIT){
            //System.out.println("im in b");
            //System.out.println(b.getFilterData().categoryBits);
            ((Enemies)b.getUserData()).headJump();
        }


        if(a.getFilterData().categoryBits == MarioBros.ENEMY_HEAD_BIT){

            ((Enemies)a.getUserData()).headJump();
        }

        if(a.getFilterData().categoryBits == MarioBros.ENEMY_BIT){

            ((Enemies)a.getUserData()).reverse(true,false);
        }

        if(b.getFilterData().categoryBits == MarioBros.ENEMY_BIT){
           // System.out.println("im in reverse");

            ((Enemies)b.getUserData()).reverse(true,false);
        }





    }

    @Override
    public void endContact(Contact contact) {

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
