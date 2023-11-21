/*
 * Copyright © 2022 Mark Raynsford <code@io7m.com> https://www.io7m.com
 *
 * Permission to use, copy, modify, and/or distribute this software for any
 * purpose with or without fee is hereby granted, provided that the above
 * copyright notice and this permission notice appear in all copies.
 *
 * THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES
 * WITH REGARD TO THIS SOFTWARE INCLUDING ALL IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY
 * SPECIAL, DIRECT, INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES
 * WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS, WHETHER IN AN
 * ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF OR
 * IN CONNECTION WITH THE USE OR PERFORMANCE OF THIS SOFTWARE.
 */

package com.io7m.jsycamore.theme.spi;

import com.io7m.jsycamore.api.spaces.SySpaceRGBAPreType;
import com.io7m.jsycamore.api.text.SyFontDescription;
import com.io7m.jsycamore.api.themes.SyThemeValueException;
import com.io7m.jsycamore.api.themes.SyThemeValueFunctionType;
import com.io7m.jsycamore.api.themes.SyThemeValueType;
import com.io7m.jsycamore.api.themes.SyThemeValueType.SyConstantType;
import com.io7m.jsycamore.api.themes.SyThemeValuesBuilderType;
import com.io7m.jsycamore.api.themes.SyThemeValuesType;
import com.io7m.jsycamore.theme.spi.internal.SyValueConstantColor4D;
import com.io7m.jsycamore.theme.spi.internal.SyValueConstantDouble;
import com.io7m.jsycamore.theme.spi.internal.SyValueConstantFont;
import com.io7m.jsycamore.theme.spi.internal.SyValueConstantInteger;
import com.io7m.jsycamore.theme.spi.internal.SyValueFunctionColor4D;
import com.io7m.jsycamore.theme.spi.internal.SyValueFunctionDouble;
import com.io7m.jsycamore.theme.spi.internal.SyValueFunctionFont;
import com.io7m.jsycamore.theme.spi.internal.SyValueFunctionInteger;
import com.io7m.jsycamore.theme.spi.internal.SyValueNodeType;
import com.io7m.jsycamore.theme.spi.internal.SyValueType;
import com.io7m.jtensors.core.parameterized.vectors.PVector4D;
import com.io7m.junreachable.UnreachableCodeException;
import org.jgrapht.graph.DirectedAcyclicGraph;
import org.jgrapht.traverse.TopologicalOrderIterator;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import static com.io7m.jsycamore.api.themes.SyThemeValueErrorCode.VALUE_ALREADY_EXISTS;
import static com.io7m.jsycamore.api.themes.SyThemeValueErrorCode.VALUE_CYCLE;
import static com.io7m.jsycamore.api.themes.SyThemeValueErrorCode.VALUE_DOES_NOT_EXIST;
import static com.io7m.jsycamore.api.themes.SyThemeValueErrorCode.VALUE_TYPE_INCORRECT;
import static com.io7m.jsycamore.theme.spi.internal.SyValueType.TYPE_COLOR_4D;
import static com.io7m.jsycamore.theme.spi.internal.SyValueType.TYPE_DOUBLE;
import static com.io7m.jsycamore.theme.spi.internal.SyValueType.TYPE_FONT;
import static com.io7m.jsycamore.theme.spi.internal.SyValueType.TYPE_INTEGER;

/**
 * A set of theme attributes.
 */

public final class SyThemeValues implements SyThemeValuesType
{
  private final HashMap<String, SyValueNodeType> nodes;
  private final DirectedAcyclicGraph<SyValueNodeType, ValueEdge> nodeGraph;
  private Map<String, SyThemeValueType> valuesRead;

  private SyThemeValues(
    final HashMap<String, SyValueNodeType> inNodes,
    final DirectedAcyclicGraph<SyValueNodeType, ValueEdge> inNodeGraph)
  {
    this.nodes =
      Objects.requireNonNull(inNodes, "inNodes");
    this.nodeGraph =
      Objects.requireNonNull(inNodeGraph, "inNodeGraph");
    this.valuesRead =
      Collections.unmodifiableMap(
        (Map<String, SyThemeValueType>) (Object) this.nodes);
  }

  /**
   * Create a new attribute builder.
   *
   * @return A new builder
   */

  public static SyThemeValuesBuilderType builder()
  {
    return new Builder();
  }

  private static void checkNodeType(
    final SyValueNodeType sourceNode,
    final SyValueType type)
    throws SyThemeValueException
  {
    if (sourceNode.type() != type) {
      throw new SyThemeValueException(
        "Attribute '%s' has type '%s' instead of type '%s'".formatted(
          sourceNode.name(),
          sourceNode.type(),
          type),
        VALUE_TYPE_INCORRECT
      );
    }
  }

  private static void checkNodeDoesNotExist(
    final Map<String, SyValueNodeType> nodes,
    final String name)
    throws SyThemeValueException
  {
    if (nodes.containsKey(name)) {
      throw new SyThemeValueException(
        "Attribute '%s' already exists".formatted(name),
        VALUE_ALREADY_EXISTS
      );
    }
  }

  private static SyValueNodeType checkNodeExists(
    final Map<String, SyValueNodeType> nodes,
    final String name)
    throws SyThemeValueException
  {
    final var sourceNode = nodes.get(name);
    if (sourceNode == null) {
      throw new SyThemeValueException(
        "Attribute '%s' does not exist".formatted(name),
        VALUE_DOES_NOT_EXIST
      );
    }
    return sourceNode;
  }

  private static <T extends SyValueNodeType> T checkNodeKind(
    final SyValueNodeType node,
    final Class<T> clazz)
    throws SyThemeValueException
  {
    if (Objects.equals(node.getClass(), clazz)) {
      return clazz.cast(node);
    }

    throw new SyThemeValueException(
      "Attribute '%s' treated as if it was a %s but it is actually a %s"
        .formatted(
          node.name(),
          attributeKind(clazz),
          attributeKind(node.getClass())),
      VALUE_TYPE_INCORRECT
    );
  }

  private static <T extends SyValueNodeType> T check(
    final Map<String, SyValueNodeType> nodes,
    final String name,
    final Class<T> clazz,
    final SyValueType type)
    throws SyThemeValueException
  {
    Objects.requireNonNull(nodes, "nodes");
    Objects.requireNonNull(name, "name");
    Objects.requireNonNull(clazz, "clazz");
    Objects.requireNonNull(type, "type");

    final var checked = checkNodeKind(checkNodeExists(nodes, name), clazz);
    checkNodeType(checked, type);
    return checked;
  }

  private static SyValueNodeType check(
    final Map<String, SyValueNodeType> nodes,
    final String name,
    final SyValueType type)
    throws SyThemeValueException
  {
    Objects.requireNonNull(nodes, "nodes");
    Objects.requireNonNull(name, "name");
    Objects.requireNonNull(type, "type");

    final var checked = checkNodeExists(nodes, name);
    checkNodeType(checked, type);
    return checked;
  }

  private static <T extends SyValueNodeType> String attributeKind(
    final Class<T> clazz)
  {
    if (Objects.equals(clazz, SyValueConstantInteger.class)) {
      return "constant integer";
    }
    if (Objects.equals(clazz, SyValueConstantDouble.class)) {
      return "constant double";
    }
    if (Objects.equals(clazz, SyValueConstantColor4D.class)) {
      return "constant RGBA color";
    }
    if (Objects.equals(clazz, SyValueConstantFont.class)) {
      return "constant font";
    }
    if (Objects.equals(clazz, SyValueFunctionInteger.class)) {
      return "function [integer]";
    }
    if (Objects.equals(clazz, SyValueFunctionFont.class)) {
      return "function [font]";
    }
    if (Objects.equals(clazz, SyValueFunctionDouble.class)) {
      return "function [double]";
    }
    if (Objects.equals(clazz, SyValueFunctionColor4D.class)) {
      return "function [RGBA color]";
    }
    throw new UnreachableCodeException();
  }

  @Override
  public Map<String, SyThemeValueType> values()
  {
    return this.valuesRead;
  }

  @Override
  public void reset()
  {
    for (final var entry : this.nodes.entrySet()) {
      final var node = entry.getValue();
      node.reset();
    }
    this.evaluate();
  }

  @Override
  public PVector4D<SySpaceRGBAPreType> color4D(
    final String name)
    throws SyThemeValueException
  {
    Objects.requireNonNull(name, "name");

    final var sourceNode = check(this.nodes, name, TYPE_COLOR_4D);
    return (PVector4D<SySpaceRGBAPreType>) sourceNode.evaluated();
  }

  @Override
  public void setColor4D(
    final String name,
    final PVector4D<SySpaceRGBAPreType> value)
    throws SyThemeValueException
  {
    Objects.requireNonNull(name, "name");
    Objects.requireNonNull(value, "color");

    final var constant =
      check(
        this.nodes,
        name,
        SyValueConstantColor4D.class,
        TYPE_COLOR_4D
      );

    constant.set(value);
    this.evaluate();
  }

  @Override
  public void setColorTransform(
    final String name,
    final SyThemeValueFunctionType<PVector4D<SySpaceRGBAPreType>> transform)
    throws SyThemeValueException
  {
    Objects.requireNonNull(name, "name");
    Objects.requireNonNull(transform, "transform");

    final var function =
      check(
        this.nodes,
        name,
        SyValueFunctionColor4D.class,
        TYPE_COLOR_4D
      );

    function.set(transform);
    this.evaluate();
  }

  @Override
  public int integer(final String name)
    throws SyThemeValueException
  {
    Objects.requireNonNull(name, "name");

    final var sourceNode =
      check(this.nodes, name, TYPE_INTEGER);

    return ((Integer) sourceNode.evaluated()).intValue();
  }

  @Override
  public void setInteger(
    final String name,
    final int value)
    throws SyThemeValueException
  {
    Objects.requireNonNull(name, "name");

    final var constant =
      check(
        this.nodes,
        name,
        SyValueConstantInteger.class,
        TYPE_INTEGER
      );

    constant.set(value);
    this.evaluate();
  }

  @Override
  public void setIntegerTransform(
    final String name,
    final SyThemeValueFunctionType<Integer> transform)
    throws SyThemeValueException
  {
    Objects.requireNonNull(name, "name");
    Objects.requireNonNull(transform, "transform");

    final var function =
      check(
        this.nodes,
        name,
        SyValueFunctionInteger.class,
        TYPE_INTEGER
      );

    function.set(transform);
    this.evaluate();
  }

  @Override
  public SyFontDescription font(final String name)
    throws SyThemeValueException
  {
    Objects.requireNonNull(name, "name");

    final var sourceNode =
      check(this.nodes, name, TYPE_FONT);

    return ((SyFontDescription) sourceNode.evaluated());
  }

  @Override
  public void setFont(
    final String name,
    final SyFontDescription value)
    throws SyThemeValueException
  {
    Objects.requireNonNull(name, "name");

    final var constant =
      check(
        this.nodes,
        name,
        SyValueConstantFont.class,
        TYPE_FONT
      );

    constant.set(value);
    this.evaluate();
  }

  @Override
  public void setFontTransform(
    final String name,
    final SyThemeValueFunctionType<SyFontDescription> transform)
    throws SyThemeValueException
  {
    Objects.requireNonNull(name, "name");
    Objects.requireNonNull(transform, "transform");

    final var function =
      check(
        this.nodes,
        name,
        SyValueFunctionFont.class,
        TYPE_FONT
      );

    function.set(transform);
    this.evaluate();
  }

  @Override
  public double double_(final String name)
    throws SyThemeValueException
  {
    Objects.requireNonNull(name, "name");

    final var sourceNode =
      check(this.nodes, name, TYPE_DOUBLE);

    return (double) sourceNode.evaluated();
  }

  @Override
  public void setDouble(
    final String name,
    final double value)
    throws SyThemeValueException
  {
    Objects.requireNonNull(name, "name");

    final var constant =
      check(
        this.nodes,
        name,
        SyValueConstantDouble.class,
        TYPE_DOUBLE
      );

    constant.set(value);
    this.evaluate();
  }

  @Override
  public void setDoubleTransform(
    final String name,
    final SyThemeValueFunctionType<Double> transform)
    throws SyThemeValueException
  {
    Objects.requireNonNull(name, "name");
    Objects.requireNonNull(transform, "transform");

    final var function =
      check(
        this.nodes,
        name,
        SyValueFunctionDouble.class,
        TYPE_DOUBLE
      );

    function.set(transform);
    this.evaluate();
  }

  private void evaluate()
  {
    final var iterator = new TopologicalOrderIterator<>(this.nodeGraph);
    while (iterator.hasNext()) {
      final var node = iterator.next();

      switch (node) {
        case final SyValueFunctionInteger functionNode -> {
          functionNode.evaluate(
            Optional.ofNullable(this.nodes.get(functionNode.source()))
              .orElseThrow(UnreachableCodeException::new)
          );
          continue;
        }
        case final SyValueFunctionDouble functionNode -> {
          functionNode.evaluate(
            Optional.ofNullable(this.nodes.get(functionNode.source()))
              .orElseThrow(UnreachableCodeException::new)
          );
          continue;
        }
        case final SyValueFunctionFont functionNode -> {
          functionNode.evaluate(
            Optional.ofNullable(this.nodes.get(functionNode.source()))
              .orElseThrow(UnreachableCodeException::new)
          );
          continue;
        }
        case final SyValueFunctionColor4D functionNode -> {
          functionNode.evaluate(
            Optional.ofNullable(this.nodes.get(functionNode.source()))
              .orElseThrow(UnreachableCodeException::new)
          );
          continue;
        }
        case final SyConstantType syConstantType -> {
          continue;
        }
      }
    }
  }

  private record ValueEdge(
    SyValueNodeType source,
    SyValueNodeType target)
  {
    ValueEdge
    {
      Objects.requireNonNull(source, "source");
      Objects.requireNonNull(target, "target");
    }
  }

  private static final class Builder implements SyThemeValuesBuilderType
  {
    private HashMap<String, SyValueNodeType> nodes;
    private DirectedAcyclicGraph<SyValueNodeType, ValueEdge> nodeGraph;

    private Builder()
    {
      this.nodes =
        new HashMap<>();
      this.nodeGraph =
        new DirectedAcyclicGraph<>(ValueEdge.class);
    }

    @Override
    public SyThemeValuesBuilderType createConstantColor4D(
      final String name,
      final String description,
      final PVector4D<SySpaceRGBAPreType> value)
      throws SyThemeValueException
    {
      Objects.requireNonNull(name, "name");
      Objects.requireNonNull(description, "description");
      Objects.requireNonNull(value, "color");

      return this.createConstant(
        name,
        description,
        value,
        TYPE_COLOR_4D);
    }

    private SyThemeValuesBuilderType createConstant(
      final String name,
      final String description,
      final Object value,
      final SyValueType type)
      throws SyThemeValueException
    {
      checkNodeDoesNotExist(this.nodes, name);

      final var node = switch (type) {
        case TYPE_COLOR_4D -> new SyValueConstantColor4D(
          name, description, (PVector4D<SySpaceRGBAPreType>) value
        );
        case TYPE_DOUBLE -> new SyValueConstantDouble(
          name, description, (Double) value
        );
        case TYPE_INTEGER -> new SyValueConstantInteger(
          name, description, (Integer) value
        );
        case TYPE_FONT -> new SyValueConstantFont(
          name, description, (SyFontDescription) value
        );
      };

      this.nodes.put(name, node);
      this.nodeGraph.addVertex(node);
      return this;
    }

    @Override
    public SyThemeValuesBuilderType createConstantInteger(
      final String name,
      final String description,
      final int value)
      throws SyThemeValueException
    {
      Objects.requireNonNull(name, "name");
      Objects.requireNonNull(description, "description");

      return this.createConstant(
        name,
        description,
        value,
        TYPE_INTEGER);
    }

    @Override
    public SyThemeValuesBuilderType createConstantFont(
      final String name,
      final String description,
      final SyFontDescription value)
      throws SyThemeValueException
    {
      Objects.requireNonNull(name, "name");
      Objects.requireNonNull(description, "description");
      Objects.requireNonNull(value, "value");

      return this.createConstant(
        name,
        description,
        value,
        TYPE_FONT);
    }

    @Override
    public SyThemeValuesBuilderType createConstantDouble(
      final String name,
      final String description,
      final double value)
      throws SyThemeValueException
    {
      Objects.requireNonNull(name, "name");
      Objects.requireNonNull(description, "description");

      return this.createConstant(
        name,
        description,
        value,
        TYPE_DOUBLE);
    }

    @Override
    public SyThemeValuesBuilderType createFunctionColor4D(
      final String name,
      final String description,
      final String source,
      final SyThemeValueFunctionType<PVector4D<SySpaceRGBAPreType>> value)
      throws SyThemeValueException
    {
      Objects.requireNonNull(name, "name");
      Objects.requireNonNull(description, "description");
      Objects.requireNonNull(source, "source");
      Objects.requireNonNull(value, "value");

      return this.createFunctionNode(
        name, description, source, value, TYPE_COLOR_4D);
    }

    private <T> SyThemeValuesBuilderType createFunctionNode(
      final String name,
      final String description,
      final String source,
      final SyThemeValueFunctionType<T> value,
      final SyValueType type)
      throws SyThemeValueException
    {
      checkNodeDoesNotExist(this.nodes, name);

      final var sourceNode = checkNodeExists(this.nodes, source);
      checkNodeType(sourceNode, type);

      final var node =
        switch (type) {
          case TYPE_COLOR_4D -> new SyValueFunctionColor4D(
            name,
            description,
            source,
            x -> (PVector4D<SySpaceRGBAPreType>) value.apply((T) x)
          );
          case TYPE_DOUBLE -> new SyValueFunctionDouble(
            name,
            description,
            source,
            x -> (Double) value.apply((T) x)
          );
          case TYPE_INTEGER -> new SyValueFunctionInteger(
            name,
            description,
            source,
            x -> (Integer) value.apply((T) x)
          );
          case TYPE_FONT -> new SyValueFunctionFont(
            name,
            description,
            source,
            x -> (SyFontDescription) value.apply((T) x)
          );
        };

      try {
        this.nodes.put(name, node);
        this.nodeGraph.addVertex(node);
        this.nodeGraph.addEdge(
          sourceNode,
          node,
          new ValueEdge(sourceNode, node)
        );
      } catch (final IllegalArgumentException e) {
        this.nodes.remove(name);
        this.nodeGraph.removeVertex(node);
        throw new SyThemeValueException(
          "Adding attribute '%s' would cause a cycle in the graph".formatted(
            name),
          VALUE_CYCLE
        );
      }

      return this;
    }

    @Override
    public SyThemeValuesBuilderType createFunctionInteger(
      final String name,
      final String description,
      final String source,
      final SyThemeValueFunctionType<Integer> value)
      throws SyThemeValueException
    {
      Objects.requireNonNull(name, "name");
      Objects.requireNonNull(description, "description");
      Objects.requireNonNull(source, "source");
      Objects.requireNonNull(value, "value");

      return this.createFunctionNode(
        name, description, source, value, TYPE_INTEGER);
    }

    @Override
    public SyThemeValuesBuilderType createFunctionDouble(
      final String name,
      final String description,
      final String source,
      final SyThemeValueFunctionType<Double> value)
      throws SyThemeValueException
    {
      Objects.requireNonNull(name, "name");
      Objects.requireNonNull(description, "description");
      Objects.requireNonNull(source, "source");
      Objects.requireNonNull(value, "value");

      return this.createFunctionNode(
        name, description, source, value, TYPE_DOUBLE);
    }

    @Override
    public SyThemeValuesBuilderType createFunctionFont(
      final String name,
      final String description,
      final String source,
      final SyThemeValueFunctionType<SyFontDescription> value)
      throws SyThemeValueException
    {
      Objects.requireNonNull(name, "name");
      Objects.requireNonNull(description, "description");
      Objects.requireNonNull(source, "source");
      Objects.requireNonNull(value, "value");

      return this.createFunctionNode(
        name, description, source, value, TYPE_FONT);
    }

    @Override
    public SyThemeValuesType create()
    {
      final var allNodes =
        this.nodes;
      final var allGraph =
        this.nodeGraph;

      this.nodes = new HashMap<>();
      this.nodeGraph = new DirectedAcyclicGraph<>(ValueEdge.class);
      final var attributes = new SyThemeValues(allNodes, allGraph);
      attributes.evaluate();
      return attributes;
    }
  }
}
