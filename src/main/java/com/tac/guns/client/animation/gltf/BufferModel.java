package com.tac.guns.client.animation.gltf;

import de.javagl.jgltf.model.io.Buffers;

import java.nio.ByteBuffer;

public class BufferModel {
    /**
     * The URI of the buffer data
     */
    private String uri;

    /**
     * The actual data of the buffer
     */
    private ByteBuffer bufferData;

    /**
     * Set the URI for the buffer data
     *
     * @param uri The URI of the buffer data
     */
    public void setUri(String uri)
    {
        this.uri = uri;
    }

    /**
     * Set the data of this buffer
     *
     * @param bufferData The buffer data
     */
    public void setBufferData(ByteBuffer bufferData)
    {
        this.bufferData = bufferData;
    }

    public String getUri()
    {
        return uri;
    }

    public int getByteLength()
    {
        return bufferData.capacity();
    }

    public ByteBuffer getBufferData()
    {
        return Buffers.createSlice(bufferData);
    }
}
