package com.io7m.jsycamore.examples.lwjgl30;

import java.util.Properties;

import javax.annotation.Nonnull;

import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jcanephora.GLException;
import com.io7m.jcanephora.GLInterfaceLWJGL30;
import com.io7m.jlog.Log;
import com.io7m.jsycamore.GUI;
import com.io7m.jsycamore.GUIException;
import com.io7m.jsycamore.geometry.Point;
import com.io7m.jsycamore.geometry.ScreenRelative;
import com.io7m.jtensors.VectorReadable2I;
import com.io7m.jvvfs.Filesystem;
import com.io7m.jvvfs.FilesystemError;
import com.io7m.jvvfs.PathReal;

final class SetupGUI
{
  static GUI setupGUI(
    final @Nonnull PathReal resource_dir,
    final @Nonnull String archive_name,
    final @Nonnull Point<ScreenRelative> viewport_position,
    final @Nonnull VectorReadable2I viewport_size)
    throws GLException,
      ConstraintError,
      FilesystemError,
      GUIException
  {
    final Properties p = new Properties();
    p.put("com.io7m.jsycamore.level", "LOG_DEBUG");
    p.put("com.io7m.jsycamore.logs.example", "true");
    p.put("com.io7m.jsycamore.logs.example.filesystem", "false");
    p.put("com.io7m.jsycamore.logs.example.jsycamore.renderer", "false");

    final Log log = new Log(p, "com.io7m.jsycamore", "example");
    final GLInterfaceLWJGL30 gl = new GLInterfaceLWJGL30(log);

    final Filesystem fs = new Filesystem(log, resource_dir);
    fs.createDirectory("/sycamore");
    fs.mount(archive_name, "/sycamore");

    return new GUI(viewport_position, viewport_size, gl, fs, log);
  }
}
