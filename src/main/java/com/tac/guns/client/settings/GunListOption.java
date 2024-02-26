package com.tac.guns.client.settings;

import com.tac.guns.interfaces.IResourceLocation;

/**
 * Author: Forked from MrCrayfish, continued by Timeless devs
 */
public class GunListOption<E extends IResourceLocation> {   /*
    private String title;
    private ResourceLocation selected;
    private Supplier<List<E>> supplier;
    private Supplier<ResourceLocation> getter;
    private Consumer<ResourceLocation> setter;
    private Function<E, Component> displayNameGetter;
    private IAdditionalRenderer renderer = (button, matrixStack, partialTicks) -> {};

    public GunListOption(String title, Supplier<List<E>> supplier, Supplier<ResourceLocation> getter, Consumer<ResourceLocation> setter, Function<E, Component> displayNameGetter)
    {
        super(title);
        this.title = title;
        this.supplier = supplier;
        this.getter = getter;
        this.setter = setter;
        this.displayNameGetter = displayNameGetter;
    }

    public GunListOption setRenderer(@Nullable IAdditionalRenderer renderer)
    {
        this.renderer = renderer;
        return this;
    }

    @Override
    public AbstractWidget createButton(Options options, int x, int y, int width)
    {
        return new OptionButton(x, y, width, 20, this, this.getTitle(), (button) -> {
            List<E> list = this.supplier.get();
            if(list.isEmpty())
                return;
            this.nextItem(Screen.hasShiftDown() ? -1 : 1);
            button.setMessage(this.getTitle());
        }) {
            @Override
            public void renderButton(PoseStack matrixStack, int mouseX, int mouseY, float partialTicks)
            {
                List<E> list = GunListOption.this.supplier.get();
                this.active = !list.isEmpty();
                super.renderButton(matrixStack, mouseX, mouseY, partialTicks);
                GunListOption.this.renderer.render(this, matrixStack, partialTicks);
            }
        };
    }

    @Nullable
    public E get()
    {
        if(this.selected == null)
        {
            this.selected = this.getter.get();
        }
        List<E> list = this.supplier.get();
        E e = list.stream().filter(c -> c.getLocation().equals(this.selected)).findFirst().orElse(null);
        if(e == null && list.size() > 0)
        {
            e = list.get(0);
            this.selected = e.getLocation();
        }
        return e;
    }

    public void set(E e)
    {
        List<E> list = this.supplier.get();
        if(list.indexOf(e) != -1)
        {
            this.setter.accept(e.getLocation());
            this.selected = e.getLocation();
        }
    }

    private void nextItem(int offset)
    {
        List<E> list = this.supplier.get();
        E current = this.get();
        if(current != null)
        {
            int nextIndex =  Math.floorMod(list.indexOf(current) + offset, list.size());
            E next = list.get(nextIndex);
            this.set(next);
        }
    }

    public Component getTitle()
    {
        Component component = Component.translatable("tac.option_list.no_items");
        E e = this.get();
        if(e != null)
        {
            component = this.displayNameGetter.apply(e);
        }
        return Component.translatable(this.title + ".format", component);
    }

    public interface IAdditionalRenderer
    {
        void render(OptionButton button, PoseStack matrixStack, float partialTicks);
    }
    */
}
