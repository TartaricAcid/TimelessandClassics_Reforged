package com.tac.guns.client.animation;

import com.tac.guns.client.animation.gltf.AnimationStructure;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnmodifiableView;

import java.util.*;

public class AnimationController {
    private final AnimationStructure animationStructure;
    private final AnimationListenerSupplier listenerSupplier;
    protected List<ObjectAnimation> prototypes;
    protected final ArrayList<ObjectAnimationRunner> currentRunners = new ArrayList<>();
    private final ArrayList<Queue<AnimationPlan>> animationQueue = new ArrayList<>();

    public AnimationController(AnimationStructure animationStructure, AnimationListenerSupplier model){
        this.animationStructure = animationStructure;
        this.listenerSupplier = model;
        prototypes = null;
    }

    public AnimationStructure getAnimationStructure(){
        return animationStructure;
    }

    public AnimationListenerSupplier getListenerSupplier(){
        return listenerSupplier;
    }

    @Nullable
    @UnmodifiableView
    public List<ObjectAnimation> getPrototypes(){
        if(prototypes == null) return null;
        return Collections.unmodifiableList(prototypes);
    }

    public void refreshPrototypes(){
        prototypes = AnimationResources.getInstance().createAnimations(animationStructure, (AnimationListenerSupplier[]) null);
    }

    @Nullable
    public ObjectAnimationRunner getAnimation(int track){
        if(track >= currentRunners.size()) return null;
        return currentRunners.get(track);
    }

    public void queueAnimation(int track, Queue<AnimationPlan> queue){
        if(prototypes == null){
            refreshPrototypes();
        }
        //ensure the capability
        for(int i = animationQueue.size(); i <= track; i++){
            animationQueue.add(null);
        }

        animationQueue.set(track, queue);

        if(queue != null){
            AnimationPlan plan = null;
            while (plan == null && !queue.isEmpty()){
                plan = queue.poll();
            }
            if(plan != null){
                run(track, plan.animationName, plan.playType, plan.transitionTimeS);
            }
        }
    }

    public void runAnimation(int track, String animationName, ObjectAnimation.PlayType playType, float transitionTimeS){
        if(prototypes == null){
            refreshPrototypes();
        }
        //运行单个动画的时候视为执行一个只有一个动画的动画队列，因此需要清理旧的队列。
        if(track < animationQueue.size()){
            animationQueue.set(track, null);
        }
        run(track, animationName, playType, transitionTimeS);
    }

    private void run(int track, String animationName, ObjectAnimation.PlayType playType, float transitionTimeS){
        for(ObjectAnimation prototype : prototypes){
            if(prototype.name.equals(animationName)){

                //ensure the capability
                for(int i = currentRunners.size(); i <= track; i++){
                    currentRunners.add(null);
                }

                ObjectAnimation animation = new ObjectAnimation(prototype);
                animation.applyAnimationListeners(listenerSupplier);
                animation.playType = playType;
                ObjectAnimationRunner runner = new ObjectAnimationRunner(animation);
                runner.setProgressNs(0);
                runner.run();

                ObjectAnimationRunner oldRunner = currentRunners.get(track);
                if(transitionTimeS > 0) {
                    if (oldRunner != null) {
                        oldRunner.transition(runner, (long) (transitionTimeS * 1e9));
                    }else{
                        currentRunners.set(track, runner);
                    }
                }else {
                    currentRunners.set(track, runner);
                }

                return;
            }
        }
    }

    public void update(){
        for (int i = 0; i < currentRunners.size(); i++){
            ObjectAnimationRunner runner = currentRunners.get(i);
            if(runner == null) continue;
            //更新当前动画runner
            runner.update();
            //更新过渡目标动画runner，并且如果过渡已经完成，将其塞进currentRunners
            if(runner.getTransitionTo() != null) {
                runner.getTransitionTo().update();
                if(!runner.isTransitioning()){
                    currentRunners.set(i, runner.getTransitionTo());
                    runner = runner.getTransitionTo();
                }
            }
            //如果动画结束，检查队列是否有下一个动画，有则播放
            if(runner.isHolding() && !runner.isTransitioning()){
                if (i < animationQueue.size()){
                    Queue<AnimationPlan> queue = animationQueue.get(i);
                    if(queue != null){
                        AnimationPlan plan = null;
                        while (plan == null && !queue.isEmpty()){
                            plan = queue.poll();
                        }
                        if(plan != null){
                            run(i, plan.animationName, plan.playType, plan.transitionTimeS);
                        }
                    }
                }
            }
        }
    }

    public static class AnimationPlan{
        public String animationName;
        public ObjectAnimation.PlayType playType;
        public float transitionTimeS;

        public AnimationPlan(String animationName, ObjectAnimation.PlayType playType, float transitionTimeS){
            this.animationName = animationName;
            this.playType = playType;
            this.transitionTimeS =  transitionTimeS;
        }
    }
}
